package com.rhain.java7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Java7 新特性一览
 * @author Rhain
 * @since 2014/8/25.
 */
public class Java7NewIO {

    // create a path  创建一个路径
    static Path listing = Paths.get("D:\\res\\tmp").normalize();
    // static Path listing2 = FileSystems.getDefault().getPath("D:\\res\\tmp").toAbsolutePath().toRealPath();

    static Path prefix = Paths.get("D:\\");
    static Path completePath = prefix.resolve("tmp/dm/hello.properties");

    public static void main(String[] args) {


        System.out.println("File Name [" + listing.getFileName() + "]");
        System.out.println("Number of Elements in the path ]" + listing.getNameCount() + "]");
        System.out.println("Parent path [" + listing.getParent() + "]");
        System.out.println("Root of path [" + listing.getRoot() + "]");
        System.out.println("Subpath from Root,2 element deep [" + listing.subpath(0, 2) + "]");

        prefix.startsWith(completePath);
        prefix.equals(completePath);

        // Path convert into a File 路径转换为文件
        completePath.toFile();

        File file = new File("");
        // File convert to Path  文件转换为路径
        file.toPath();

        // Finding files in directory 目录中寻找文件
        Path dir = Paths.get("D:\\res\\tmp");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.jpg")) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Walking the directory tree
        try {
            Files.walkFileTree(dir, new FindJpgVisitor());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating and deleting files  创建和删除文件
        Path target = Paths.get("D:\\res\\test.txt");
        try {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-rw");
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
            Path f = Files.createFile(target, attr);

            Files.delete(target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // copying and moving files 复制和移动文件

        Path source = Paths.get("D:\\res\\test.txt");
        Path dest = Paths.get("E:\\res\\test.txt");
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);

            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * File attributes  查看文件属性
         */

        // Basic file attributes
        try {
            System.out.println(Files.getLastModifiedTime(target));
            System.out.println(Files.size(target));
            System.out.println(Files.isSymbolicLink(target));
            System.out.println(Files.isDirectory(target));
            System.out.println(Files.readAttributes(target, "*"));
            System.out.println(Files.getOwner(target));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Specific file attribute support 支持特殊的文件属性
        try {
            PosixFileAttributes attrs = Files.readAttributes(target, PosixFileAttributes.class);
            Set<PosixFilePermission> posixPermissions = attrs.permissions();
            posixPermissions.clear();
            String owner = attrs.owner().getName();
            String perms = PosixFilePermissions.toString(posixPermissions);
            System.out.format("%s %s%n", owner, perms);
            posixPermissions.add(PosixFilePermission.OWNER_READ);
            posixPermissions.add(PosixFilePermission.GROUP_READ);
            posixPermissions.add(PosixFilePermission.OTHERS_READ);
            posixPermissions.add(PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(target, posixPermissions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Symbolic links Java7可以操作文件链接
        try {
            if (Files.isSymbolicLink(target)) {
                target = Files.readSymbolicLink(target);
            }
            Files.readAttributes(target,BasicFileAttributes.class);
            //not follow a symbolic link
            Files.readAttributes(target,BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Reading and writing data quickly  更加便捷快速的读写文件
         */

        //reading file
        Path logFile = Paths.get("D:\\res\\logs\\app.log");
        try(BufferedReader reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {   //Files.newInputStream(logFile,StandardCharsets.UTF_8);
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //writing file
        Path log = Paths.get("D:\\res\\logs\\test.log");
        try(BufferedWriter writer = Files.newBufferedWriter(log,StandardCharsets.UTF_8,StandardOpenOption.WRITE)) { //Files.newOutputStream(log,StandardCharsets.UTF_8,StandardOpenOption.WRITE);
            writer.write("Hello Java 7");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Simplifying reading and writing
        try {
            Path sLog = Paths.get("D:\\res\\logs\\slog.log");
            List<String> lines = Files.readAllLines(sLog,StandardCharsets.UTF_8);
            byte[] bytes = Files.readAllBytes(sLog);

            Files.write(sLog,lines,StandardCharsets.UTF_8,StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File change notification  文件改动通知

        boolean shutdown = false;

        try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path watchDir = FileSystems.getDefault().getPath("D:\\res");
            WatchKey key = watchDir.register(watcher,StandardWatchEventKinds.ENTRY_MODIFY);
            while(!shutdown){
                key = watcher.take();
                for(WatchEvent<?> event:key.pollEvents()){
                    if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY){
                        System.out.println("D:\\\res changed");
                    }
                }
                key.reset();
            }
        } catch (IOException |InterruptedException e) {
            e.printStackTrace();
        }

        //SeekableByteChannel    read the last 1000 characters from a log file
        try {
            Path seekFile = Paths.get("D:\\res\\seek.txt");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel channel = FileChannel.open(seekFile,StandardOpenOption.READ);
            channel.read(buffer,channel.size() - 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Asynchronous I/O operations  异步文件操作
         */


        //Future style
        try{
            Path lFile = Paths.get("D:\\res\\large.txt");
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(lFile);

            ByteBuffer buffer = ByteBuffer.allocate(100_100);
            Future<Integer> result = channel.read(buffer,0);

            while(!result.isDone()){
                //Todo other task
            }
            Integer bytesRead = result.get();
            System.out.println("Byte read["+bytesRead+"]");
        } catch (IOException | InterruptedException |ExecutionException  e) {
            e.printStackTrace();
        }


        //Callback style
        try{
            Path cFile = Paths.get("D:\\res\\large.txt");
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(cFile);

            ByteBuffer buffer = ByteBuffer.allocate(100_100);

            channel.read(buffer,0,buffer,new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("Bytes read["+result+"]");
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println(exc.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //NetworkChannel
        SelectorProvider provider = SelectorProvider.provider();
        try{
            NetworkChannel socketChannel = provider.openSocketChannel();
            SocketAddress address = new InetSocketAddress(3080);
            socketChannel = socketChannel.bind(address);

            Set<SocketOption<?>> socketOptions = socketChannel.supportedOptions();
            System.out.println(socketOptions.toString());

            socketChannel.setOption(StandardSocketOptions.IP_TOS,3);
            Boolean keepAlive = socketChannel.getOption(StandardSocketOptions.SO_KEEPALIVE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //MulticastChannel  common use case for peer-to-peer networking applications,such as BitTorrent
        try{
            NetworkInterface networkInterface = NetworkInterface.getByName("net1");  //select network interface

            DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET);  //open DatagramChannel
            dc.setOption(StandardSocketOptions.SO_REUSEADDR,true);
            dc.bind(new InetSocketAddress(8200));
            dc.setOption(StandardSocketOptions.IP_MULTICAST_IF,networkInterface);    //set channel to multicast

            InetAddress group = InetAddress.getByName("10.10.7.120");
            MembershipKey key = dc.join(group,networkInterface);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class FindJpgVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toString().endsWith(".jpg")) {
                System.out.println(file.getFileName());
            }
            return FileVisitResult.CONTINUE;
        }
    }

}

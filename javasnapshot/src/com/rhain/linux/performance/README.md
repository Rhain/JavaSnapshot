###Linux下常用性能分析工具

####vmstat

vmstat 是Virtual Memory Statistics(虚拟内存统计)的缩写。可以对操作系统的内存信息，进程状态，CPU活动进行监视。

vmstat [-V] [-n] [delay [count]]

vmstat 3 5  每3秒更新一次输出信息，统计5次


####sar

sar命令，可以全面获取系统的CPU、运行队列、磁盘I/O、分页（交换区）、内存、CPU中断、网络等性能数据。

sar [options] [-o filename] [interval] [count]

####iostat

iostat对系统的磁盘I/O操作进行监视。

iostat [-c | id] [-k] [-t] [-x [device]] [interval] [count]


####free

free监视Linux内存使用状况。


####uptime

统计系统当前的运行状况。


####netstat

用于显示本机网络连接、运行端口、路由表等信心


####top

提供了实时对系统处理状态的监控，能够实时显示系统中各个进程的资源占用情况。

top的交互式指令：

- h或者? :显示帮助信息，给出交互式命令的一些总结或者说明总结
- k： 终止一个进程，系统将提示用户输入一个需要终止的进程PID
- i：忽略闲置进程和僵死进程。
- s：改变top输出信息两次刷新之间的时间。
- o或者O：改变top输出信息中显示项目的顺序。按小写a~z键可以将相应的列向右移动，按大写的A~Z可以将相应的列向左移动。最后按回车键确定。
- f或者F：从当前显示列表中添加或者删除项目
- m：**切换显示内存信息**
- t：**切换显示进程和cpu状态信息**
- r：重新设置一个进程的优先级
- l：**切换显示平均负载和启动时间信息**
- q：退出top显示
- c：**切换显示完整命令行和命令名称信息**
- M：**根据驻留内存大小进行排序输出**
- P：**根据CPU使用百分比大小进行排序输出**
- T：**根据时间/累计时间进行排序输出**
- S：切换到累计模式
- W：将当前top设置写入~/.toprc文件中
摘自：`https://blog.csdn.net/fgf00/article/details/77544325`

sysctl.conf工作原理
sysctl命令被用于在内核运行时动态地修改内核的运行参数，可用的内核参数在目录/proc/sys中。它包含一些TCP/IP堆栈和虚拟内存系统的高级选项， 这可以让有经验的管理员提高引人注目的系统性能。用sysctl可以读取设置超过五百个系统变量。

```
# sysctl 变量的设置通常是字符串、数字或者布尔型。 (布尔型用 1 来表示’yes’，用 0 来表示’no’)

net.ipv4.ip_forward = 0     # 路由转发功能，1为开启

net.ipv4.conf.default.rp_filter = 1     #   1：通过反向路径回溯进行源地址验证(在RFC1812中定义)。对于单穴主机和stub网络路
由器推荐使用该选项。0：不通过反向路径回溯进行源地址验证。

net.ipv4.conf.default.accept_source_route = 0   # 处理无源路由的包。

kernel.sysrq = 0    # 0 (禁用SysRq) 或1 (启用SsyRq) 激活键盘上的sysrq按键。这个按键用于给内核传递信息，用于紧急情况下重启系统。
当遇到死机或者没有响应的时候，甚至连 tty 都进不去，可以尝试用 SysRq
重启计算机。在终端上同时按Alt, SysRq和命令键则会执行SysRq命令,
SysRq键就是"Print Screen"健. 比如Alt+SysRq+b则重启机器

kernel.core_uses_pid = 1    #   Core文件的文件名是否添加应用程序pid做为扩展 0：不添加 1：添加

net.ipv4.tcp_syncookies = 1     # 开启SYN洪水攻击保护 0：关闭  1：打开

kernel.msgmnb = 65536   # 单个消息队列中允许的最大字节长度(限制单个消息队列中所有消息包含的字节数之和)

kernel.msgmax = 65536   # 消息队列中单个消息的最大字节数

kernel.shmmax = 68719476736     # 系统所允许的最大共享内存段的大小（以字节为单位）。

kernel.shmall = 4294967296      # 系统上可以使用的共享内存的总量（以字节为单位）。

net.ipv4.tcp_max_tw_buckets = 60000     #   系统在同时所处理的最大timewait sockets 数目。如果超过此数的话，time-wait socket
会被立即砍除并且显示警告信息。

net.ipv4.tcp_sack = 1   # 0：不启用1：启用。 表示是否启用有选择的应答（Selective
Acknowledgment），这可以通过有选择地应答乱序接收到的报文来提高性能（这样可以
让发送者只发送丢失的报文段）；（对于广域网通信来说）这个选项应该启用，但是这
会增加对 CPU 的占用。

net.ipv4.tcp_window_scaling = 1     # 表示设置tcp/ip会话的滑动窗口大小是否可变。0：不可变 1：可变

net.core.wmem_default = 8388608     # 设置发送的socket缺省缓存大小(字节)
net.core.rmem_default = 8388608     # 设置接收socket的缺省缓存大小(字节)
net.core.rmem_max = 16777216    # 设置接收socket的最大缓存大小(字节)
net.core.wmem_max = 16777216    # 设置发送的socket最大缓存大小(字节)

net.core.netdev_max_backlog = 262144    # 设置当个别接口接收包的速度快于内核处理速度时允许的最大的包序列

net.core.somaxconn = 256    # 定义了系统中每一个端口最大的监听队列的长度，这是个全局的参数。

net.core.optmem_max = 81920     # 表示每个socket所允许的最大缓冲区的大小(字节)

net.ipv4.tcp_max_orphans = 3276800  # 系统所能处理不属于任何进程的TCP
sockets最大数量。假如超过这个数量，那么不属于任何进程的连接会被立即reset，
并同时显示警告信息。之所以要设定这个限制，纯粹为了抵御那些简单的 DoS
攻击，千万不要依赖这个或是人为的降低这个限制。

net.ipv4.tcp_max_syn_backlog = 262144   # 对于那些依然还未获得客户端确认的连接请求，需要保存在队列中最大数目。
默认值是1024，可提高到2048。

net.ipv4.tcp_timestamps = 0     # 表示是否启用以一种比超时重发更精确的方法（请参阅 RFC 1323）来启用对 RTT
的计算；为了实现更好的性能应该启用这个选项。 0：不启用 1：启用

net.ipv4.tcp_synack_retries = 1     #  syn-ack握手状态重试次数，默认5，遭受syn-flood攻击时改为1或2。这是三次握手的
第二个步骤。值必须为正整数，并不能超过 255。因为每一次重新发送封包都会耗费
约 30 至 40 秒去等待才决定尝试下一次重新发送或决定放弃。缺省值为
5，即每一个连线要在约 180 秒 (3 分钟) 后才确定逾时.

net.ipv4.tcp_syn_retries = 1    # 表示本机向外发起TCP SYN连接超时重传的次数，不应该高于255；该值仅仅针对外出的连接，
对于进来的连接由tcp_retries1控制。

net.ipv4.tcp_tw_recycle = 1     # 打开快速 TIME-WAIT sockets 回收。默认为0，表示关闭

net.ipv4.tcp_tw_reuse = 1   # 是否允许重新应用处于TIME-WAIT状态的socket用于新的TCP连接，
即开启重用。默认为0，表示关闭；

net.ipv4.tcp_mem = 94500000 915000000 927000000     # 该文件保存了三个值，分别是
# low：当TCP使用了低于该值的内存页面数时，TCP不会考虑释放内存。
# presure：当TCP使用了超过该值的内存页面数量时，TCP试图稳定其内存使用，
进入pressure模式，当内存消耗低于low值时则退出pressure状态。
# high：允许所有tcp sockets用于排队缓冲数据报的页面量。

net.ipv4.tcp_rmem = 4096 87380 8388608  # # 增加TCP最大缓冲区大小 此文件中保存有三个值，分别是
# Min：为TCP socket预留用于接收缓冲的内存最小值。每个tcp socket都可以在建立后使用它。
即使在内存出现紧张情况下tcp socket都至少会有这么多数量的内存用于接收缓冲
# Default：为TCP socket预留用于接收缓冲的内存数量，默认情况下该值会影响
其它协议使用的net.core.rmem_default 值，一般要低于net.core.rmem_default的值。
该值决定了在tcp_adv_win_scale、tcp_app_win和tcp_app_win=0默认值情况下，
TCP窗口大小为65535。
# Max：用于TCP socket接收缓冲的内存最大值。该值不会影响net.core.rmem_max，
"静态"选择参数SO_SNDBUF则不受该值影响。

net.ipv4.tcp_wmem = 4096 87380 8388608  # # 增加TCP最大缓冲区大小
此文件中保存有三个值，分别是
# Min：为TCP socket预留用于发送缓冲的内存最小值。每个tcp socket都可以在建立后使用它。
# Default：为TCP socket预留用于发送缓冲的内存数量，默认情况下该值会影响
其它协议使用的net.core.wmem_default 值，一般要低于net.core.wmem_default的值。
# Max：用于TCP socket发送缓冲的内存最大值。该值不会影响net.core.wmem_max，
"静态"选择参数SO_SNDBUF则不受该值影响。

net.ipv4.tcp_keepalive_time = 1200  # 表示从最后一个包结束后多少秒内没有活动，
才发送keepalive包保持连接，默认7200s，理想可设为1800s，即如果非正常断开，
1800s后可通过keepalive知道。

net.ipv4.tcp_keepalive_intvl = 60   # 表示发送TCP探测的频率，乘以tcp_keepalive_probes表示断开没有相应的TCP连接的时间。

net.ipv4.tcp_keepalive_probes = 3   #   该文件表示丢弃TCP连接前，进行最大TCP保持连接侦测的次数。
保持连接仅在SO_KEEPALIVE套接字选项被打开时才被发送。

net.ipv4.tcp_fin_timeout = 30   # 表示如果套接字由本端要求关闭，
这个参数决定了它保持在FIN-WAIT-2状态的时间。默认值为 60 秒。

vm.swappiness = 0   # 调整内存参数，当内存使用率不足0%（开始是默认值60）
时在使用swap，尽量避免使用swap，减少唤醒软中断进程kswapd，从而降低kswapd
进程对cpu的占用。

net.ipv4.ip_local_port_range = 1024  65535  # 本地发起连接时使用的端口范围，tcp初始化时会修改此值

net.ipv4.ip_local_reserved_ports = 16001,19001,20001,21001-21006,41622,8000-10000
# 规划出一段端口段预留作为服务的端口，避免被随机端口给占用掉。
```

limits.conf设置
1）暂时生效，适用于通过 ulimit 命令登录 shell 会话期间

ulimit -SHn 65535

2）永久生效，通过将一个相应的 ulimit 语句添加到由登录 shell 读取的文件之一（例如 ~/.profile），即特定于 shell 的用户资源文件；或者通过编辑/etc/security/limits.conf 
比如添加到/etc/profile

```
echo ulimit -SHn 65535 >> /etc/profile
source /etc/profile
```

**修改最大进程和最大文件打开数限制**

```shell
vi /etc/security/limits.conf
* soft nproc 11000
* hard nproc 11000
* soft nofile 655350
* hard nofile 655350

sed -i '/# End of file/ i\*       soft    nofile          65535' /etc/security/limits.conf
sed -i '/# End of file/ i\*       hard    nofile          65535' /etc/security/limits.conf

sed -i 's/*          soft    nproc     1024/*          soft    nproc     65535/g' /etc/security/limits.d/90-nproc.conf
```


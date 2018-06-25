#!/bin/bash

#########################################
## sync to /etc/profile.d/myPriNetlips-net-config.sh
#########################################

# create 2 table

exists=$( cat /etc/iproute2/rt_tables | grep "200 myPriNet" | wc -l )

if [ $exists -eq 1 ]; then
  echo "already exists route talbes"
else
  echo "200 myPriNet" >> /etc/iproute2/rt_tables
  echo "201 myPubNet" >> /etc/iproute2/rt_tables
fi

iptables -F
## add internal network rules myPriNet
ip route flush table myPriNet
ip route add default via [pri.net.route.ip] dev eth0 src [current.pri.net.ip] table myPriNet
ip rule add from [current.pri.net.ip] table myPriNet

## add myPubNet network rules myPubNet
ip route flush table myPubNet
ip route add default via [pub.net.route.ip] dev eth1 src [current.pub.net.ip] table myPubNet
ip rule add from [current.pub.net.ip] table myPubNet

# nslookup code1.emi.myPriNetlips.com
ip rule add to [pri.network.segment.ip.1] table myPriNet
ip rule add to [pri.network.segment.ip.2] table myPriNet
ip rule add to [pri.network.segment.ip.n] table myPriNet

echo -ne "\n\t Success.\n"

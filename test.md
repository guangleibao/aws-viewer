
# VPC: gl-vpc|vpc-23e78947, 10.0.0.0/16, tenancy: default

## Internet Gateway:
+ П IGW-1: gl-vpc-igw|igw-d36f4fb6
+ TTL-IGW:1


## Virtual Private Gateway:
+ П VGW-1: dropme-vgw|vgw-05779bbe199192de4, ipsec.1, ASN:64512, available
+ TTL-VGW:1


## Route Table:
+ ⌧ RT-1: gl-vpc-default-rt|rtb-6d4cef09, main:Yes
	- #1: 10.0.0.0/16, local, active, propagated:No
	- #2: pl-66a5400f, vpce-0b7d2811e1568ce5e, active, propagated:No
+ ⌧ RT-2: gl-vpc-public-rt|rtb-f450f390, main:No
	- #1: 10.0.0.0/16, local, active, propagated:No
	- #2: 0.0.0.0/0, igw-d36f4fb6, active, propagated:No
	- #3: pl-66a5400f, vpce-0b7d2811e1568ce5e, active, propagated:No
+ TTL-Route-Table:2


## Network ACL:
+ ▢ NACL-1: gl-vpc-default-nacl|acl-09b4156d, default:Yes
	- ⬅ 100, IcmpTypeCode:null, -1, PortRange:null, Dsc: 0.0.0.0/0, allow
	- ⬅ 32767, IcmpTypeCode:null, -1, PortRange:null, Dsc: 0.0.0.0/0, deny
	- ➪ 100, IcmpTypeCode:null, -1, PortRange:null, Src: 0.0.0.0/0, allow
	- ➪ 32767, IcmpTypeCode:null, -1, PortRange:null, Src: 0.0.0.0/0, deny
+ TTL-NACL:1


## Security Group:
+ ▣ SG-1: {null|EC2ContainerService-ecs-test-EcsSecurityGroup-VW9KC4JTMNYY|sg-010503b35bd131b3a}
	- ➪ protocol: tcp, 80-80, Src: 0.0.0.0/0, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-2: {ElasticMapReduce-master|ElasticMapReduce-master|sg-064ca361}
	- ➪ protocol: tcp, 0-65535, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ➪ protocol: tcp, 22-22, Src: 0.0.0.0/0, 
	- ➪ protocol: tcp, 8443-8443, Src: 54.222.0.0/20, 
	- ➪ protocol: udp, 0-65535, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ➪ protocol: icmp, -1--1, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-3: {gl-vpc-rds-sg|gl-vpc-rds-sg|sg-0b78d3b36f01594f9}
	- ➪ protocol: tcp, 5439-5439, Src: gl-vpc-bastion-sg, 
	- ➪ protocol: tcp, 1433-1433, Src: gl-vpc-bastion-sg, 
	- ➪ protocol: tcp, 1521-1521, Src: gl-vpc-bastion-sg, 
	- ➪ protocol: tcp, 5432-5432, Src: gl-vpc-bastion-sg, 
	- ➪ protocol: tcp, 3306-3306, Src: gl-vpc-bastion-sg, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-4: {gl-vpc-pq-sg|gl-vpc-pq-sg|sg-4b8f602c}
	- ➪ protocol: tcp, 5432-5432, Src: gl-vpc-bastion-sg, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-5: {gl-vpc-default-sg|default|sg-80c5d6e4}
	- ➪ protocol: -1, null-null, Src: gl-vpc-default-sg, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-6: {ElasticMapReduce-slave|ElasticMapReduce-slave|sg-864ba4e1}
	- ➪ protocol: tcp, 0-65535, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ➪ protocol: udp, 0-65535, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ➪ protocol: icmp, -1--1, Src: ElasticMapReduce-master, ElasticMapReduce-slave, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-7: {gl-vpc-redshift-sg|gl-vpc-redshift-sg|sg-cbb25dac}
	- ➪ protocol: tcp, 5439-5439, Src: gl-vpc-bastion-sg, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-8: {gl-vpc-webserver-sg|gl-vpc-webserver-sg|sg-e5c22d82}
	- ➪ protocol: tcp, 80-80, Src: 10.0.0.0/16, 0.0.0.0/0, 
	- ➪ protocol: -1, null-null, Src: gl-vpc-webserver-sg, gl-vpc-bastion-sg, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-9: {gl-vpc-elb-sg|gl-vpc-elb-sg|sg-efe40b88}
	- ➪ protocol: tcp, 80-80, Src: 0.0.0.0/0, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ ▣ SG-10: {gl-vpc-bastion-sg|gl-vpc-bastion-sg|sg-f7cad993}
	- ➪ protocol: tcp, 80-80, Src: 0.0.0.0/0, 
	- ➪ protocol: tcp, 22-22, Src: 0.0.0.0/0, 
	- ⬅ protocol: -1, null-null, Dsc: 0.0.0.0/0, 
+ TTL-SG:10


## Elastic Load Balancing V2 - Application & Network:
+ Ⰰ ELB-1-network: gl-vpc-example-nlb, internet-facing, ipv4, active, dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
		* listener: TCP, 80, ssl:null
		* load_balancing.cross_zone.enabled:false, deletion_protection.enabled:false, 
		* 
+ Ⰰ ELB-2-application: gl-vpc-example-alb, internet-facing, ipv4, active, dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- @(gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); (gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); 
		* listener: HTTP, 80, ssl:null
		* access_logs.s3.enabled:false, access_logs.s3.bucket:, access_logs.s3.prefix:, 
		* idle_timeout.timeout_seconds:60, deletion_protection.enabled:false, routing.http2.enabled:true, 
+ TTL-ELBv2:2


## Elastic Load Balancing - Classic:
+ Ⰰ ELB-1-classic: gl-vpc-classic-elb, internet-facing, , dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
		* listener: HTTP, 80, ssl:null
		* ConnectionDraining(Enabled=true, Timeout=300), ConnectionSettings(IdleTimeout=60), CrossZoneLoadBalancing(Enabled=true), AccessLog(Enabled=false), 
+ TTL-ELB:1


## VPC Endpoint - Gateway & Interface:
+ ⵚ VPC-Endpoint-1-Gateway: dropme-vpce|vpce-0b7d2811e1568ce5e, to-svc:com.amazonaws.cn-north-1.dynamodb, state:null
	- published-in: gl-vpc-default-rt|rtb-6d4cef09, gl-vpc-public-rt|rtb-f450f390, 
+ ⵚ VPC-Endpoint-2-Interface: dropme-vpce-2|vpce-0ccefd3d6217b2553, to-svc:com.amazonaws.cn-north-1.sns, state:null
	- @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
		* eni-1-vpc_endpoint: cn-north-1a, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, n/a, n/a}
			* protected-by: gl-vpc-bastion-sg, 
		* eni-2-vpc_endpoint: cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, n/a, n/a}
			* protected-by: gl-vpc-bastion-sg, 
		* dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
		* dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
		* dns: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
+ TTL-ENDPOINT:2


## VPC Peering Connection - Requester & Accepter:
+ ⚯ PEER-Request-1: dropme-peering, pcx-095fc5583512adfd5 to 172.31.0.0/16 of vpc-00120462, Active
+ TTL-PEERING-REQ:1
+ TTL-PEERING-ACC:0


## Elastic Container Service:
+ ⊍ ECS-1: ecs-test, ACTIVE, instances:4, service:0, task(run/pending):0/0
	- instance: arn:aws-cn:ecs:cn-north-1:░░░░░░░░░░░░:container-instance/1bf51895-d73f-4633-a75d-98224e3c2cb8
		* Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0220cf45477e5571a, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░░
			* eni-0: interface, cn-north-1b
	- instance: arn:aws-cn:ecs:cn-north-1:░░░░░░░░░░░░:container-instance/3adda783-040a-4894-9813-ad6277194fe8
		* Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0e5804d42bd77553d, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 54.░░░░░░░░░░░
			* eni-0: interface, cn-north-1b
	- instance: arn:aws-cn:ecs:cn-north-1:░░░░░░░░░░░░:container-instance/8cfc6094-9dbc-43df-8c6d-bfd862aff607
		* Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0e7b6a11acbb931b4, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
			* eni-0: interface, cn-north-1a
	- instance: arn:aws-cn:ecs:cn-north-1:░░░░░░░░░░░░:container-instance/b8e6b53f-4d3c-4d7c-8f3e-4e765d100b7a
		* Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0d91a6986df490c1b, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░, 52.░░░░░░░░░░
			* eni-0: interface, cn-north-1a
+ TTL-ECS:1


## ElasticSearch:
+ ⫸ ES-Domain-1: test-es-domain, 6.4, master-count:3, master-type:m4.large.elasticsearch, instance-count:2, instance-type:c4.2xlarge.elasticsearch, gp2, 10Gx2, iops:null
	- endpoint-vpc: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- protected-by: {gl-vpc-default-sg|default|sg-80c5d6e4}, 
	- @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
+ TTL-ES:1


## Lambda:
+ Ⲗ LAMBDA-1: tagger, arn:aws-cn:lambda:cn-north-1:░░░░░░░░░░░░:function:tagger, $LATEST, runtime:python3.6, handler:lambda_function.lambda_handler, mem:128m, ttl:15s
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:role/gl-tagger-lambda-role
	- protected-by: {gl-vpc-bastion-sg|gl-vpc-bastion-sg|sg-f7cad993}, 
	- @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
+ TTL-LAMBDA:1


## ElastiCache:
+ ₵ MEMCACHED-1: test-mem, memcached, 1.5.10, cache.t2.small, available, atRestEnc:false, transitEnc:false, nodes: 2
	- endpoint: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- maintenance-utc: sat:18:30-sat:19:30
	- protected-by: gl-vpc-default-sg, 
	- dropme-redis-group: @(gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); 
+ ₵ REDIS-1: test-tc, redis, 5.0.4, cache.t2.small, available, atRestEnc:false, transitEnc:false, shard:2, replica:3
	- endpoint: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- maintenance-utc: fri:19:00-fri:20:00
	- protected-by: gl-vpc-default-sg, 
	- dropme-redis-group: @(gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); 
+ TTL-MEMCACHE:1
+ TTL-REDIS:1


## RDS:
+ ↁ RDS-1: orcl, DB:ORCL, oracle-ee, 11.2.0.4.v17, status:available, multi-AZ:false, admin:░░░░░░, size:20G, gp2, iops:gp2-rule
	- endpoint: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- maintenance-utc: wed:10:32-wed:11:02, backup-utc:06:13-06:43
	- protected-by: gl-vpc-default-sg, 
	- currently-at: cn-north-1b
	- gl-vpc-rds-group: @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a); (gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b); 
+ TTL-RDS:1


## Redshift:
+ TTL-REDSHIFT:0


## Auto Scaling Group:
+ ᭚ ASG-1: EC2ContainerService-ecs-test-EcsInstanceAsg-1CHNQ23XFZ4IL, lc:EC2ContainerService-ecs-test-EcsInstanceLc-1W7EIYL5ELPTV, status:null, hc-type:EC2
	- az:[cn-north-1b, cn-north-1a]
	- desired:4, max:4, min:0, registered:4
+ ᭚ ASG-2: gl-bastion-asg-v1, lc:gl-bastion-lc-v41, status:null, hc-type:EC2
	- az:[cn-north-1b, cn-north-1a]
	- desired:1, max:1, min:1, registered:1
+ ᭚ ASG-3: gl-webserver-asg-v1, lc:gl-webserver-lc-v3, status:null, hc-type:ELB
	- az:[cn-north-1b, cn-north-1a]
	- desired:2, max:2, min:2, registered:2
+ TTL-ASG:3


## SUBNET-1: @(gl-vpc-public-sn-1)(10.0.0.0/24)(subnet-f9cb799d)(cn-north-1a);  RT: gl-vpc-public-rt, NACL: gl-vpc-default-nacl

### NAT Gateway:
+ TTL-NATGW:0


### EMR:
+ TTL-EMR:0


### EC2: Auto Scaling = ★	Auto Recovery = ♺	NONE-AS-AR = ⌦
+ ★  EC2-1: Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0e7b6a11acbb931b4, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/ecsInstanceRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:30G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:22G, type:gp2, iops:100, enc:false, device:/dev/xvdcz, snapshot:NO
	- eni-0: interface, cn-north-1a, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: EC2ContainerService-ecs-test-EcsSecurityGroup-VW9KC4JTMNYY, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░
+ ★  EC2-2: Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0d91a6986df490c1b, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░, 52.░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/ecsInstanceRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:30G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:22G, type:gp2, iops:100, enc:false, device:/dev/xvdcz, snapshot:NO
	- eni-0: interface, cn-north-1a, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░░}
		* protected-by: EC2ContainerService-ecs-test-EcsSecurityGroup-VW9KC4JTMNYY, 
		* address-association: 10.░░░░░░, 52.░░░░░░░░░░
+ ★  EC2-3: Name:{webserver}, i-023fa653b4f065b83, t2.large, sriov:n/a, ena:true, running, 10.░░░░░░, 52.░░░░░░░░░░
	- role: n/a
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:50G, type:gp2, iops:150, enc:false, device:/dev/xvda, snapshot:YES
	- eni-0: interface, cn-north-1a, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░░}
		* protected-by: gl-vpc-webserver-sg, 
		* address-association: 10.░░░░░░, 52.░░░░░░░░░░
+ TTL-SUBNET-EC2:3


## SUBNET-2: @(gl-vpc-private-sn-1)(10.0.2.0/24)(subnet-72ca7816)(cn-north-1a);  RT: gl-vpc-default-rt, NACL: gl-vpc-default-nacl

### NAT Gateway:
+ TTL-NATGW:0


### EMR:
+ TTL-EMR:0


### EC2: Auto Scaling = ★	Auto Recovery = ♺	NONE-AS-AR = ⌦
+ TTL-SUBNET-EC2:0


## SUBNET-3: @(gl-vpc-private-sn-4)(10.0.3.0/24)(subnet-234b7754)(cn-north-1b);  RT: gl-vpc-default-rt, NACL: gl-vpc-default-nacl

### NAT Gateway:
+ TTL-NATGW:0


### EMR:
+ TTL-EMR:0


### EC2: Auto Scaling = ★	Auto Recovery = ♺	NONE-AS-AR = ⌦
+ TTL-SUBNET-EC2:0


## SUBNET-4: @(gl-vpc-public-sn-2)(10.0.1.0/24)(subnet-e74a7690)(cn-north-1b);  RT: gl-vpc-public-rt, NACL: gl-vpc-default-nacl

### NAT Gateway:
+ TTL-NATGW:0


### EMR:
+ Ⰺ EMR-1: demo-emr, j-1KAO7RVO8ME9F, release:emr-5.20.0, WAITING, security-config:null
	- application: Pig 0.17.0, Pig 0.17.0, Ganglia 3.7.2, Hive 2.3.4, Hue 4.3.0, Spark 2.4.0, HBase 1.4.8
	- master: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
	- service-role: EMR_DefaultRole
	- auto-scaling-role: EMR_AutoScaling_DefaultRole
+ TTL-EMR:1


### EC2: Auto Scaling = ★	Auto Recovery = ♺	NONE-AS-AR = ⌦
+ ★  EC2-4: Name:{bastion}, i-0b424c3f43cf59c4e, c5.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/gl-bastion-ec2-role
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:waypoint, size:200G, type:gp2, iops:600, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: gl-vpc-bastion-sg, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░
+ ★  EC2-5: Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0220cf45477e5571a, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/ecsInstanceRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:30G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:22G, type:gp2, iops:100, enc:false, device:/dev/xvdcz, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░░}
		* protected-by: EC2ContainerService-ecs-test-EcsSecurityGroup-VW9KC4JTMNYY, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░░
+ ★  EC2-6: Name:{ECS Instance - EC2ContainerService-ecs-test}, i-0e5804d42bd77553d, m4.large, sriov:n/a, ena:true, running, 10.░░░░░░░, 54.░░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/ecsInstanceRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:30G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:22G, type:gp2, iops:100, enc:false, device:/dev/xvdcz, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░░░}
		* protected-by: EC2ContainerService-ecs-test-EcsSecurityGroup-VW9KC4JTMNYY, 
		* address-association: 10.░░░░░░░, 54.░░░░░░░░░░░
+ ⌦  EC2-7: Name:{null}, i-0de813faccb99ef05, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░, 54.░░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░░░}
		* protected-by: ElasticMapReduce-master, 
		* address-association: 10.░░░░░, 54.░░░░░░░░░░░
+ ⌦  EC2-8: Name:{null}, i-0432a25e8f2a4262a, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 54.░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 54.░░░░░░░░░░
+ ⌦  EC2-9: Name:{null}, i-059a5b05f823e3c2d, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░
+ ⌦  EC2-10: Name:{null}, i-00d7d686304f1fb31, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░, 54.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░, 54.░░░░░░░░░
+ ⌦  EC2-11: Name:{null}, i-05ca127c846261e23, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░
+ ⌦  EC2-12: Name:{null}, i-04966c5c90ba89531, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░
+ ⌦  EC2-13: Name:{null}, i-0b6c63a93196c9081, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░
+ ⌦  EC2-14: Name:{null}, i-0adee6ddadd6bda86, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░, 54.░░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░, 54.░░░░░░░░░░
+ ⌦  EC2-15: Name:{null}, i-0d3eaf29afba43df2, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░, 52.░░░░░░░░░
+ ⌦  EC2-16: Name:{null}, i-0c6f1ea03fdc6e87a, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░░, 52.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░░, 52.░░░░░░░░░
+ ⌦  EC2-17: Name:{null}, i-0f3d6e547d138bccc, m4.xlarge, sriov:n/a, ena:true, running, 10.░░░░░░, 54.░░░░░░░░░
	- role: arn:aws-cn:iam::░░░░░░░░░░░░:instance-profile/EMR_EC2_DefaultRole
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:32G, type:gp2, iops:100, enc:false, device:/dev/sdb, snapshot:NO
	- ebs: tag-name:null, size:10G, type:gp2, iops:100, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 54.░░░░░░░░░}
		* protected-by: ElasticMapReduce-slave, 
		* address-association: 10.░░░░░░, 54.░░░░░░░░░
+ ★  EC2-18: Name:{webserver}, i-0ee0c9b17fe1391f1, t2.large, sriov:n/a, ena:true, running, 10.░░░░░░, 52.░░░░░░░░░
	- role: n/a
	- key: ░░░░░░░░░░░░
	- ebs: tag-name:null, size:50G, type:gp2, iops:150, enc:false, device:/dev/xvda, snapshot:NO
	- eni-0: interface, cn-north-1b, primary:{░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 10.░░░░░░, ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░, 52.░░░░░░░░░}
		* protected-by: gl-vpc-webserver-sg, 
		* address-association: 10.░░░░░░, 52.░░░░░░░░░
+ TTL-SUBNET-EC2:15

+ TTL-VPC-EC2:18


## End VPC: gl-vpc

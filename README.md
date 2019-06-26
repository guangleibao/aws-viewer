# aws-viewer
Read only tools (in Java) for AWS resources. It's tested on Mac not Windows.

## Tools
1. Show resources within VPC: **`showVpc`**
	- Supported services: *EC2, ASG, EMR, ES, ECS, VPC Endpoint, Redshift, RDS, ElastiCache, Lambda, ELB, ELBv2, SG, NACL, Route Table*.

## Configure
1. Create an IAM user and generate access key and secret.
2. Grant **`ReadOnlyAccess`** AWS managed policy to above IAM user. 
3. Follow the [guide](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html) to config named profiles for above IAM user.

## Install
1. Install [maven](https://maven.apache.org/install.html).
2. Run **`$ mvn package`**

## Run
+ Get help:
	- Run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar`**
		* mode support: redact, plain.
		* print-mode support: console, markdown. 
+ For example, if the VPC name is **`demo-vpc`** and the named profile is **`demo-profile`**:
	- Run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showVpc demo-vpc redact console demo-profile`** in redact mode (recommanded).
 	- Or, run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showVpc demo-vpc plain console demo-profile`** in plain mode.

## More Information about the Usage
+ **`showVpc`** can help checking the reliability pillar of [Well-Architected practices](https://aws.amazon.com/architecture/well-architected/).

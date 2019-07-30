# aws-viewer
Read only tools (in Java) for AWS resources. It's tested on Mac not Windows.

## Tools
1. Show resources within VPC in HTML format: **`showVpc`**
	- Supported services: *EC2, ASG, EMR, ES, ECS, VPC Endpoint, Redshift, RDS, ElastiCache, Lambda, ELB, ELBv2, SG, NACL, Route Table*.
	- For example: `showVpc demo-vpc redact demo-profile > demo.html`
2. Show all instance types and are there sufficient capacities for instance types: **`showInstanceType`**
	- For example: `showInstanceType 1 demo-profile`
3. Show all regions: **`showRegionCode`**
4. Show services in a region: **`showServiceByRegionCode`**
5. Compare services in two regions: **`showServiceCompareByRegionCode`**

## Configure
1. Create an IAM user and generate access key and secret.
2. Grant **`ReadOnlyAccess`** AWS managed policy to above IAM user. For global regions the policy ARN is: `arn:aws:iam::aws:policy/ReadOnlyAccess`, for China regions the policy ARN is: `arn:aws-cn:iam::aws:policy/ReadOnlyAccess`. As it's name, the policy grants read permissions only.
3. Follow the [guide](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html) to config named profiles for above IAM user.
4. `showInstanceType` might need ec2:runInstances permission, but do not worry it uses dry run, it will not generate cost.

## Install
1. Install [maven](https://maven.apache.org/install.html).
2. Run **`$ mvn package`**

## Get Help
+ Get general help:
	- Run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar`**
+ Get tool help:
	- Use `-h`, For example, run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showVpc -h`**

## Run
+ For example, if the VPC name is **`demo-vpc`** and the named profile is **`demo-profile`**:
	- Run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showVpc demo-vpc redact demo-profile > demo.html`** in redact mode (recommanded).
 	- Or, run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showVpc demo-vpc plain demo-profile > demo.html`** in plain mode.
+ If you want to check what instance type you can use in specified capacity (such as 1) with named profile **`demo-profile`**:
	- Run **`$ java -jar target/awsviewer-1.0-SNAPSHOT-jar-with-dependencies.jar showInstanceType 1 demo-profile`**

## More Information about the Usage
+ **`showVpc`** can help checking the reliability pillar of [Well-Architected practices](https://aws.amazon.com/architecture/well-architected/), or use it as a simple troubleshooting tool for VPC resources.

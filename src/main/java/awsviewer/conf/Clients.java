package awsviewer.conf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.TreeMap;

import awsviewer.inf.CUtil;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.core.client.builder.SdkClientBuilder;
import software.amazon.awssdk.regions.Region;

/**
 * All AWS service clients related routines are here, it's maintained by human and Biu7.testUpdateConfClientsClasses
 */
public class Clients {

    public static Speaker SK = Speaker.getConsoleInstance();

    //// Put in bglutil.conf.Clients, BEGIN:2.5.69
    public static final TreeMap<String,String> ALL_SERVICES = new TreeMap<String,String>();
    // acm:1
    public static final String ACM = "software.amazon.awssdk.services.acm.AcmClient";
    // acmpca:2
    public static final String ACMPCA = "software.amazon.awssdk.services.acmpca.AcmPcaClient";
    // alexaforbusiness:3
    public static final String ALEXAFORBUSINESS = "software.amazon.awssdk.services.alexaforbusiness.AlexaForBusinessClient";
    // amplify:4
    public static final String AMPLIFY = "software.amazon.awssdk.services.amplify.AmplifyClient";
    // apigateway:5
    public static final String APIGATEWAY = "software.amazon.awssdk.services.apigateway.ApiGatewayClient";
    // apigatewaymanagementapi:6
    public static final String APIGATEWAYMANAGEMENTAPI = "software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient";
    // apigatewayv2:7
    public static final String APIGATEWAYV2 = "software.amazon.awssdk.services.apigatewayv2.ApiGatewayV2Client";
    // applicationautoscaling:8
    public static final String APPLICATIONAUTOSCALING = "software.amazon.awssdk.services.applicationautoscaling.ApplicationAutoScalingClient";
    // applicationdiscovery:9
    public static final String APPLICATIONDISCOVERY = "software.amazon.awssdk.services.applicationdiscovery.ApplicationDiscoveryClient";
    // appmesh:10
    public static final String APPMESH = "software.amazon.awssdk.services.appmesh.AppMeshClient";
    // appstream:11
    public static final String APPSTREAM = "software.amazon.awssdk.services.appstream.AppStreamClient";
    // appsync:12
    public static final String APPSYNC = "software.amazon.awssdk.services.appsync.AppSyncClient";
    // athena:13
    public static final String ATHENA = "software.amazon.awssdk.services.athena.AthenaClient";
    // autoscaling:14
    public static final String AUTOSCALING = "software.amazon.awssdk.services.autoscaling.AutoScalingClient";
    // autoscalingplans:15
    public static final String AUTOSCALINGPLANS = "software.amazon.awssdk.services.autoscalingplans.AutoScalingPlansClient";
    // backup:16
    public static final String BACKUP = "software.amazon.awssdk.services.backup.BackupClient";
    // batch:17
    public static final String BATCH = "software.amazon.awssdk.services.batch.BatchClient";
    // budgets:18
    public static final String BUDGETS = "software.amazon.awssdk.services.budgets.BudgetsClient";
    // chime:19
    public static final String CHIME = "software.amazon.awssdk.services.chime.ChimeClient";
    // cloud9:20
    public static final String CLOUD9 = "software.amazon.awssdk.services.cloud9.Cloud9Client";
    // clouddirectory:21
    public static final String CLOUDDIRECTORY = "software.amazon.awssdk.services.clouddirectory.CloudDirectoryClient";
    // cloudformation:22
    public static final String CLOUDFORMATION = "software.amazon.awssdk.services.cloudformation.CloudFormationClient";
    // cloudfront:23
    public static final String CLOUDFRONT = "software.amazon.awssdk.services.cloudfront.CloudFrontClient";
    // cloudhsm:24
    public static final String CLOUDHSM = "software.amazon.awssdk.services.cloudhsm.CloudHsmClient";
    // cloudhsmv2:25
    public static final String CLOUDHSMV2 = "software.amazon.awssdk.services.cloudhsmv2.CloudHsmV2Client";
    // cloudsearch:26
    public static final String CLOUDSEARCH = "software.amazon.awssdk.services.cloudsearch.CloudSearchClient";
    // cloudsearchdomain:27
    public static final String CLOUDSEARCHDOMAIN = "software.amazon.awssdk.services.cloudsearchdomain.CloudSearchDomainClient";
    // cloudtrail:28
    public static final String CLOUDTRAIL = "software.amazon.awssdk.services.cloudtrail.CloudTrailClient";
    // cloudwatch:29
    public static final String CLOUDWATCH = "software.amazon.awssdk.services.cloudwatch.CloudWatchClient";
    // cloudwatchevents:30
    public static final String CLOUDWATCHEVENTS = "software.amazon.awssdk.services.cloudwatchevents.CloudWatchEventsClient";
    // cloudwatchlogs:31
    public static final String CLOUDWATCHLOGS = "software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient";
    // codebuild:32
    public static final String CODEBUILD = "software.amazon.awssdk.services.codebuild.CodeBuildClient";
    // codecommit:33
    public static final String CODECOMMIT = "software.amazon.awssdk.services.codecommit.CodeCommitClient";
    // codedeploy:34
    public static final String CODEDEPLOY = "software.amazon.awssdk.services.codedeploy.CodeDeployClient";
    // codepipeline:35
    public static final String CODEPIPELINE = "software.amazon.awssdk.services.codepipeline.CodePipelineClient";
    // codestar:36
    public static final String CODESTAR = "software.amazon.awssdk.services.codestar.CodeStarClient";
    // cognitoidentity:37
    public static final String COGNITOIDENTITY = "software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient";
    // cognitoidentityprovider:38
    public static final String COGNITOIDENTITYPROVIDER = "software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient";
    // cognitosync:39
    public static final String COGNITOSYNC = "software.amazon.awssdk.services.cognitosync.CognitoSyncClient";
    // comprehend:40
    public static final String COMPREHEND = "software.amazon.awssdk.services.comprehend.ComprehendClient";
    // comprehendmedical:41
    public static final String COMPREHENDMEDICAL = "software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient";
    // config:42
    public static final String CONFIG = "software.amazon.awssdk.services.config.ConfigClient";
    // connect:43
    public static final String CONNECT = "software.amazon.awssdk.services.connect.ConnectClient";
    // costandusagereport:44
    public static final String COSTANDUSAGEREPORT = "software.amazon.awssdk.services.costandusagereport.CostAndUsageReportClient";
    // costexplorer:45
    public static final String COSTEXPLORER = "software.amazon.awssdk.services.costexplorer.CostExplorerClient";
    // databasemigration:46
    public static final String DATABASEMIGRATION = "software.amazon.awssdk.services.databasemigration.DatabaseMigrationClient";
    // datapipeline:47
    public static final String DATAPIPELINE = "software.amazon.awssdk.services.datapipeline.DataPipelineClient";
    // datasync:48
    public static final String DATASYNC = "software.amazon.awssdk.services.datasync.DataSyncClient";
    // dax:49
    public static final String DAX = "software.amazon.awssdk.services.dax.DaxClient";
    // devicefarm:50
    public static final String DEVICEFARM = "software.amazon.awssdk.services.devicefarm.DeviceFarmClient";
    // directconnect:51
    public static final String DIRECTCONNECT = "software.amazon.awssdk.services.directconnect.DirectConnectClient";
    // directory:52
    public static final String DIRECTORY = "software.amazon.awssdk.services.directory.DirectoryClient";
    // dlm:53
    public static final String DLM = "software.amazon.awssdk.services.dlm.DlmClient";
    // docdb:54
    public static final String DOCDB = "software.amazon.awssdk.services.docdb.DocDbClient";
    // dynamodb:55
    public static final String DYNAMODB = "software.amazon.awssdk.services.dynamodb.DynamoDbClient";
    // ec2:56
    public static final String EC2 = "software.amazon.awssdk.services.ec2.Ec2Client";
    // ecr:57
    public static final String ECR = "software.amazon.awssdk.services.ecr.EcrClient";
    // ecs:58
    public static final String ECS = "software.amazon.awssdk.services.ecs.EcsClient";
    // efs:59
    public static final String EFS = "software.amazon.awssdk.services.efs.EfsClient";
    // eks:60
    public static final String EKS = "software.amazon.awssdk.services.eks.EksClient";
    // elasticache:61
    public static final String ELASTICACHE = "software.amazon.awssdk.services.elasticache.ElastiCacheClient";
    // elasticbeanstalk:62
    public static final String ELASTICBEANSTALK = "software.amazon.awssdk.services.elasticbeanstalk.ElasticBeanstalkClient";
    // elasticloadbalancing:63
    public static final String ELASTICLOADBALANCING = "software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient";
    // elasticloadbalancingv2:64
    public static final String ELASTICLOADBALANCINGV2 = "software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client";
    // elasticsearch:65
    public static final String ELASTICSEARCH = "software.amazon.awssdk.services.elasticsearch.ElasticsearchClient";
    // elastictranscoder:66
    public static final String ELASTICTRANSCODER = "software.amazon.awssdk.services.elastictranscoder.ElasticTranscoderClient";
    // emr:67
    public static final String EMR = "software.amazon.awssdk.services.emr.EmrClient";
    // firehose:68
    public static final String FIREHOSE = "software.amazon.awssdk.services.firehose.FirehoseClient";
    // fms:69
    public static final String FMS = "software.amazon.awssdk.services.fms.FmsClient";
    // fsx:70
    public static final String FSX = "software.amazon.awssdk.services.fsx.FSxClient";
    // gamelift:71
    public static final String GAMELIFT = "software.amazon.awssdk.services.gamelift.GameLiftClient";
    // glacier:72
    public static final String GLACIER = "software.amazon.awssdk.services.glacier.GlacierClient";
    // globalaccelerator:73
    public static final String GLOBALACCELERATOR = "software.amazon.awssdk.services.globalaccelerator.GlobalAcceleratorClient";
    // glue:74
    public static final String GLUE = "software.amazon.awssdk.services.glue.GlueClient";
    // greengrass:75
    public static final String GREENGRASS = "software.amazon.awssdk.services.greengrass.GreengrassClient";
    // groundstation:76
    public static final String GROUNDSTATION = "software.amazon.awssdk.services.groundstation.GroundStationClient";
    // guardduty:77
    public static final String GUARDDUTY = "software.amazon.awssdk.services.guardduty.GuardDutyClient";
    // health:78
    public static final String HEALTH = "software.amazon.awssdk.services.health.HealthClient";
    // iam:79
    public static final String IAM = "software.amazon.awssdk.services.iam.IamClient";
    // inspector:80
    public static final String INSPECTOR = "software.amazon.awssdk.services.inspector.InspectorClient";
    // iot:81
    public static final String IOT = "software.amazon.awssdk.services.iot.IotClient";
    // iot1clickdevices:82
    public static final String IOT1CLICKDEVICES = "software.amazon.awssdk.services.iot1clickdevices.Iot1ClickDevicesClient";
    // iot1clickprojects:83
    public static final String IOT1CLICKPROJECTS = "software.amazon.awssdk.services.iot1clickprojects.Iot1ClickProjectsClient";
    // iotanalytics:84
    public static final String IOTANALYTICS = "software.amazon.awssdk.services.iotanalytics.IoTAnalyticsClient";
    // iotdataplane:85
    public static final String IOTDATAPLANE = "software.amazon.awssdk.services.iotdataplane.IotDataPlaneClient";
    // iotevents:86
    public static final String IOTEVENTS = "software.amazon.awssdk.services.iotevents.IotEventsClient";
    // ioteventsdata:87
    public static final String IOTEVENTSDATA = "software.amazon.awssdk.services.ioteventsdata.IotEventsDataClient";
    // iotjobsdataplane:88
    public static final String IOTJOBSDATAPLANE = "software.amazon.awssdk.services.iotjobsdataplane.IotJobsDataPlaneClient";
    // iotthingsgraph:89
    public static final String IOTTHINGSGRAPH = "software.amazon.awssdk.services.iotthingsgraph.IoTThingsGraphClient";
    // kafka:90
    public static final String KAFKA = "software.amazon.awssdk.services.kafka.KafkaClient";
    // kinesis:91
    public static final String KINESIS = "software.amazon.awssdk.services.kinesis.KinesisClient";
    // kinesisanalytics:92
    public static final String KINESISANALYTICS = "software.amazon.awssdk.services.kinesisanalytics.KinesisAnalyticsClient";
    // kinesisanalyticsv2:93
    public static final String KINESISANALYTICSV2 = "software.amazon.awssdk.services.kinesisanalyticsv2.KinesisAnalyticsV2Client";
    // kinesisvideo:94
    public static final String KINESISVIDEO = "software.amazon.awssdk.services.kinesisvideo.KinesisVideoClient";
    // kinesisvideoarchivedmedia:95
    public static final String KINESISVIDEOARCHIVEDMEDIA = "software.amazon.awssdk.services.kinesisvideoarchivedmedia.KinesisVideoArchivedMediaClient";
    // kinesisvideomedia:96
    public static final String KINESISVIDEOMEDIA = "software.amazon.awssdk.services.kinesisvideomedia.KinesisVideoMediaClient";
    // kms:97
    public static final String KMS = "software.amazon.awssdk.services.kms.KmsClient";
    // lambda:98
    public static final String LAMBDA = "software.amazon.awssdk.services.lambda.LambdaClient";
    // lexmodelbuilding:99
    public static final String LEXMODELBUILDING = "software.amazon.awssdk.services.lexmodelbuilding.LexModelBuildingClient";
    // lexruntime:100
    public static final String LEXRUNTIME = "software.amazon.awssdk.services.lexruntime.LexRuntimeClient";
    // licensemanager:101
    public static final String LICENSEMANAGER = "software.amazon.awssdk.services.licensemanager.LicenseManagerClient";
    // lightsail:102
    public static final String LIGHTSAIL = "software.amazon.awssdk.services.lightsail.LightsailClient";
    // machinelearning:103
    public static final String MACHINELEARNING = "software.amazon.awssdk.services.machinelearning.MachineLearningClient";
    // macie:104
    public static final String MACIE = "software.amazon.awssdk.services.macie.MacieClient";
    // managedblockchain:105
    public static final String MANAGEDBLOCKCHAIN = "software.amazon.awssdk.services.managedblockchain.ManagedBlockchainClient";
    // marketplacecommerceanalytics:106
    public static final String MARKETPLACECOMMERCEANALYTICS = "software.amazon.awssdk.services.marketplacecommerceanalytics.MarketplaceCommerceAnalyticsClient";
    // marketplaceentitlement:107
    public static final String MARKETPLACEENTITLEMENT = "software.amazon.awssdk.services.marketplaceentitlement.MarketplaceEntitlementClient";
    // marketplacemetering:108
    public static final String MARKETPLACEMETERING = "software.amazon.awssdk.services.marketplacemetering.MarketplaceMeteringClient";
    // mediaconnect:109
    public static final String MEDIACONNECT = "software.amazon.awssdk.services.mediaconnect.MediaConnectClient";
    // mediaconvert:110
    public static final String MEDIACONVERT = "software.amazon.awssdk.services.mediaconvert.MediaConvertClient";
    // medialive:111
    public static final String MEDIALIVE = "software.amazon.awssdk.services.medialive.MediaLiveClient";
    // mediapackage:112
    public static final String MEDIAPACKAGE = "software.amazon.awssdk.services.mediapackage.MediaPackageClient";
    // mediapackagevod:113
    public static final String MEDIAPACKAGEVOD = "software.amazon.awssdk.services.mediapackagevod.MediaPackageVodClient";
    // mediastore:114
    public static final String MEDIASTORE = "software.amazon.awssdk.services.mediastore.MediaStoreClient";
    // mediastoredata:115
    public static final String MEDIASTOREDATA = "software.amazon.awssdk.services.mediastoredata.MediaStoreDataClient";
    // mediatailor:116
    public static final String MEDIATAILOR = "software.amazon.awssdk.services.mediatailor.MediaTailorClient";
    // migrationhub:117
    public static final String MIGRATIONHUB = "software.amazon.awssdk.services.migrationhub.MigrationHubClient";
    // mobile:118
    public static final String MOBILE = "software.amazon.awssdk.services.mobile.MobileClient";
    // mq:119
    public static final String MQ = "software.amazon.awssdk.services.mq.MqClient";
    // mturk:120
    public static final String MTURK = "software.amazon.awssdk.services.mturk.MTurkClient";
    // neptune:121
    public static final String NEPTUNE = "software.amazon.awssdk.services.neptune.NeptuneClient";
    // opsworks:122
    public static final String OPSWORKS = "software.amazon.awssdk.services.opsworks.OpsWorksClient";
    // opsworkscm:123
    public static final String OPSWORKSCM = "software.amazon.awssdk.services.opsworkscm.OpsWorksCmClient";
    // organizations:124
    public static final String ORGANIZATIONS = "software.amazon.awssdk.services.organizations.OrganizationsClient";
    // personalize:125
    public static final String PERSONALIZE = "software.amazon.awssdk.services.personalize.PersonalizeClient";
    // personalizeevents:126
    public static final String PERSONALIZEEVENTS = "software.amazon.awssdk.services.personalizeevents.PersonalizeEventsClient";
    // personalizeruntime:127
    public static final String PERSONALIZERUNTIME = "software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient";
    // pi:128
    public static final String PI = "software.amazon.awssdk.services.pi.PiClient";
    // pinpoint:129
    public static final String PINPOINT = "software.amazon.awssdk.services.pinpoint.PinpointClient";
    // pinpointemail:130
    public static final String PINPOINTEMAIL = "software.amazon.awssdk.services.pinpointemail.PinpointEmailClient";
    // pinpointsmsvoice:131
    public static final String PINPOINTSMSVOICE = "software.amazon.awssdk.services.pinpointsmsvoice.PinpointSmsVoiceClient";
    // polly:132
    public static final String POLLY = "software.amazon.awssdk.services.polly.PollyClient";
    // pricing:133
    public static final String PRICING = "software.amazon.awssdk.services.pricing.PricingClient";
    // quicksight:134
    public static final String QUICKSIGHT = "software.amazon.awssdk.services.quicksight.QuickSightClient";
    // ram:135
    public static final String RAM = "software.amazon.awssdk.services.ram.RamClient";
    // rds:136
    public static final String RDS = "software.amazon.awssdk.services.rds.RdsClient";
    // rdsdata:137
    public static final String RDSDATA = "software.amazon.awssdk.services.rdsdata.RdsDataClient";
    // redshift:138
    public static final String REDSHIFT = "software.amazon.awssdk.services.redshift.RedshiftClient";
    // rekognition:139
    public static final String REKOGNITION = "software.amazon.awssdk.services.rekognition.RekognitionClient";
    // resourcegroups:140
    public static final String RESOURCEGROUPS = "software.amazon.awssdk.services.resourcegroups.ResourceGroupsClient";
    // resourcegroupstaggingapi:141
    public static final String RESOURCEGROUPSTAGGINGAPI = "software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient";
    // robomaker:142
    public static final String ROBOMAKER = "software.amazon.awssdk.services.robomaker.RoboMakerClient";
    // route53:143
    public static final String ROUTE53 = "software.amazon.awssdk.services.route53.Route53Client";
    // route53domains:144
    public static final String ROUTE53DOMAINS = "software.amazon.awssdk.services.route53domains.Route53DomainsClient";
    // route53resolver:145
    public static final String ROUTE53RESOLVER = "software.amazon.awssdk.services.route53resolver.Route53ResolverClient";
    // s3:146
    public static final String S3 = "software.amazon.awssdk.services.s3.S3Client";
    // sagemaker:147
    public static final String SAGEMAKER = "software.amazon.awssdk.services.sagemaker.SageMakerClient";
    // sagemakerruntime:148
    public static final String SAGEMAKERRUNTIME = "software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient";
    // secretsmanager:149
    public static final String SECRETSMANAGER = "software.amazon.awssdk.services.secretsmanager.SecretsManagerClient";
    // securityhub:150
    public static final String SECURITYHUB = "software.amazon.awssdk.services.securityhub.SecurityHubClient";
    // serverlessapplicationrepository:151
    public static final String SERVERLESSAPPLICATIONREPOSITORY = "software.amazon.awssdk.services.serverlessapplicationrepository.ServerlessApplicationRepositoryClient";
    // servicecatalog:152
    public static final String SERVICECATALOG = "software.amazon.awssdk.services.servicecatalog.ServiceCatalogClient";
    // servicediscovery:153
    public static final String SERVICEDISCOVERY = "software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient";
    // ses:154
    public static final String SES = "software.amazon.awssdk.services.ses.SesClient";
    // sfn:155
    public static final String SFN = "software.amazon.awssdk.services.sfn.SfnClient";
    // shield:156
    public static final String SHIELD = "software.amazon.awssdk.services.shield.ShieldClient";
    // signer:157
    public static final String SIGNER = "software.amazon.awssdk.services.signer.SignerClient";
    // sms:158
    public static final String SMS = "software.amazon.awssdk.services.sms.SmsClient";
    // snowball:159
    public static final String SNOWBALL = "software.amazon.awssdk.services.snowball.SnowballClient";
    // sns:160
    public static final String SNS = "software.amazon.awssdk.services.sns.SnsClient";
    // sqs:161
    public static final String SQS = "software.amazon.awssdk.services.sqs.SqsClient";
    // ssm:162
    public static final String SSM = "software.amazon.awssdk.services.ssm.SsmClient";
    // storagegateway:163
    public static final String STORAGEGATEWAY = "software.amazon.awssdk.services.storagegateway.StorageGatewayClient";
    // sts:164
    public static final String STS = "software.amazon.awssdk.services.sts.StsClient";
    // support:165
    public static final String SUPPORT = "software.amazon.awssdk.services.support.SupportClient";
    // swf:166
    public static final String SWF = "software.amazon.awssdk.services.swf.SwfClient";
    // textract:167
    public static final String TEXTRACT = "software.amazon.awssdk.services.textract.TextractClient";
    // transcribe:168
    public static final String TRANSCRIBE = "software.amazon.awssdk.services.transcribe.TranscribeClient";
    // transcribestreaming:169
    // transfer:170
    public static final String TRANSFER = "software.amazon.awssdk.services.transfer.TransferClient";
    // translate:171
    public static final String TRANSLATE = "software.amazon.awssdk.services.translate.TranslateClient";
    // waf:172
    public static final String WAF = "software.amazon.awssdk.services.waf.WafClient";
    // workdocs:173
    public static final String WORKDOCS = "software.amazon.awssdk.services.workdocs.WorkDocsClient";
    // worklink:174
    public static final String WORKLINK = "software.amazon.awssdk.services.worklink.WorkLinkClient";
    // workmail:175
    public static final String WORKMAIL = "software.amazon.awssdk.services.workmail.WorkMailClient";
    // workspaces:176
    public static final String WORKSPACES = "software.amazon.awssdk.services.workspaces.WorkSpacesClient";
    // xray:177
    public static final String XRAY = "software.amazon.awssdk.services.xray.XRayClient";
    static {
        ALL_SERVICES.put("acm","software.amazon.awssdk.services.acm.AcmClient");
        ALL_SERVICES.put("acmpca","software.amazon.awssdk.services.acmpca.AcmPcaClient");
        ALL_SERVICES.put("alexaforbusiness","software.amazon.awssdk.services.alexaforbusiness.AlexaForBusinessClient");
        ALL_SERVICES.put("amplify","software.amazon.awssdk.services.amplify.AmplifyClient");
        ALL_SERVICES.put("apigateway","software.amazon.awssdk.services.apigateway.ApiGatewayClient");
        ALL_SERVICES.put("apigatewaymanagementapi","software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient");
        ALL_SERVICES.put("apigatewayv2","software.amazon.awssdk.services.apigatewayv2.ApiGatewayV2Client");
        ALL_SERVICES.put("applicationautoscaling","software.amazon.awssdk.services.applicationautoscaling.ApplicationAutoScalingClient");
        ALL_SERVICES.put("applicationdiscovery","software.amazon.awssdk.services.applicationdiscovery.ApplicationDiscoveryClient");
        ALL_SERVICES.put("appmesh","software.amazon.awssdk.services.appmesh.AppMeshClient");
        ALL_SERVICES.put("appstream","software.amazon.awssdk.services.appstream.AppStreamClient");
        ALL_SERVICES.put("appsync","software.amazon.awssdk.services.appsync.AppSyncClient");
        ALL_SERVICES.put("athena","software.amazon.awssdk.services.athena.AthenaClient");
        ALL_SERVICES.put("autoscaling","software.amazon.awssdk.services.autoscaling.AutoScalingClient");
        ALL_SERVICES.put("autoscalingplans","software.amazon.awssdk.services.autoscalingplans.AutoScalingPlansClient");
        ALL_SERVICES.put("backup","software.amazon.awssdk.services.backup.BackupClient");
        ALL_SERVICES.put("batch","software.amazon.awssdk.services.batch.BatchClient");
        ALL_SERVICES.put("budgets","software.amazon.awssdk.services.budgets.BudgetsClient");
        ALL_SERVICES.put("chime","software.amazon.awssdk.services.chime.ChimeClient");
        ALL_SERVICES.put("cloud9","software.amazon.awssdk.services.cloud9.Cloud9Client");
        ALL_SERVICES.put("clouddirectory","software.amazon.awssdk.services.clouddirectory.CloudDirectoryClient");
        ALL_SERVICES.put("cloudformation","software.amazon.awssdk.services.cloudformation.CloudFormationClient");
        ALL_SERVICES.put("cloudfront","software.amazon.awssdk.services.cloudfront.CloudFrontClient");
        ALL_SERVICES.put("cloudhsm","software.amazon.awssdk.services.cloudhsm.CloudHsmClient");
        ALL_SERVICES.put("cloudhsmv2","software.amazon.awssdk.services.cloudhsmv2.CloudHsmV2Client");
        ALL_SERVICES.put("cloudsearch","software.amazon.awssdk.services.cloudsearch.CloudSearchClient");
        ALL_SERVICES.put("cloudsearchdomain","software.amazon.awssdk.services.cloudsearchdomain.CloudSearchDomainClient");
        ALL_SERVICES.put("cloudtrail","software.amazon.awssdk.services.cloudtrail.CloudTrailClient");
        ALL_SERVICES.put("cloudwatch","software.amazon.awssdk.services.cloudwatch.CloudWatchClient");
        ALL_SERVICES.put("cloudwatchevents","software.amazon.awssdk.services.cloudwatchevents.CloudWatchEventsClient");
        ALL_SERVICES.put("cloudwatchlogs","software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient");
        ALL_SERVICES.put("codebuild","software.amazon.awssdk.services.codebuild.CodeBuildClient");
        ALL_SERVICES.put("codecommit","software.amazon.awssdk.services.codecommit.CodeCommitClient");
        ALL_SERVICES.put("codedeploy","software.amazon.awssdk.services.codedeploy.CodeDeployClient");
        ALL_SERVICES.put("codepipeline","software.amazon.awssdk.services.codepipeline.CodePipelineClient");
        ALL_SERVICES.put("codestar","software.amazon.awssdk.services.codestar.CodeStarClient");
        ALL_SERVICES.put("cognitoidentity","software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient");
        ALL_SERVICES.put("cognitoidentityprovider","software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient");
        ALL_SERVICES.put("cognitosync","software.amazon.awssdk.services.cognitosync.CognitoSyncClient");
        ALL_SERVICES.put("comprehend","software.amazon.awssdk.services.comprehend.ComprehendClient");
        ALL_SERVICES.put("comprehendmedical","software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient");
        ALL_SERVICES.put("config","software.amazon.awssdk.services.config.ConfigClient");
        ALL_SERVICES.put("connect","software.amazon.awssdk.services.connect.ConnectClient");
        ALL_SERVICES.put("costandusagereport","software.amazon.awssdk.services.costandusagereport.CostAndUsageReportClient");
        ALL_SERVICES.put("costexplorer","software.amazon.awssdk.services.costexplorer.CostExplorerClient");
        ALL_SERVICES.put("databasemigration","software.amazon.awssdk.services.databasemigration.DatabaseMigrationClient");
        ALL_SERVICES.put("datapipeline","software.amazon.awssdk.services.datapipeline.DataPipelineClient");
        ALL_SERVICES.put("datasync","software.amazon.awssdk.services.datasync.DataSyncClient");
        ALL_SERVICES.put("dax","software.amazon.awssdk.services.dax.DaxClient");
        ALL_SERVICES.put("devicefarm","software.amazon.awssdk.services.devicefarm.DeviceFarmClient");
        ALL_SERVICES.put("directconnect","software.amazon.awssdk.services.directconnect.DirectConnectClient");
        ALL_SERVICES.put("directory","software.amazon.awssdk.services.directory.DirectoryClient");
        ALL_SERVICES.put("dlm","software.amazon.awssdk.services.dlm.DlmClient");
        ALL_SERVICES.put("docdb","software.amazon.awssdk.services.docdb.DocDbClient");
        ALL_SERVICES.put("dynamodb","software.amazon.awssdk.services.dynamodb.DynamoDbClient");
        ALL_SERVICES.put("ec2","software.amazon.awssdk.services.ec2.Ec2Client");
        ALL_SERVICES.put("ecr","software.amazon.awssdk.services.ecr.EcrClient");
        ALL_SERVICES.put("ecs","software.amazon.awssdk.services.ecs.EcsClient");
        ALL_SERVICES.put("efs","software.amazon.awssdk.services.efs.EfsClient");
        ALL_SERVICES.put("eks","software.amazon.awssdk.services.eks.EksClient");
        ALL_SERVICES.put("elasticache","software.amazon.awssdk.services.elasticache.ElastiCacheClient");
        ALL_SERVICES.put("elasticbeanstalk","software.amazon.awssdk.services.elasticbeanstalk.ElasticBeanstalkClient");
        ALL_SERVICES.put("elasticloadbalancing","software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient");
        ALL_SERVICES.put("elasticloadbalancingv2","software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client");
        ALL_SERVICES.put("elasticsearch","software.amazon.awssdk.services.elasticsearch.ElasticsearchClient");
        ALL_SERVICES.put("elastictranscoder","software.amazon.awssdk.services.elastictranscoder.ElasticTranscoderClient");
        ALL_SERVICES.put("emr","software.amazon.awssdk.services.emr.EmrClient");
        ALL_SERVICES.put("firehose","software.amazon.awssdk.services.firehose.FirehoseClient");
        ALL_SERVICES.put("fms","software.amazon.awssdk.services.fms.FmsClient");
        ALL_SERVICES.put("fsx","software.amazon.awssdk.services.fsx.FSxClient");
        ALL_SERVICES.put("gamelift","software.amazon.awssdk.services.gamelift.GameLiftClient");
        ALL_SERVICES.put("glacier","software.amazon.awssdk.services.glacier.GlacierClient");
        ALL_SERVICES.put("globalaccelerator","software.amazon.awssdk.services.globalaccelerator.GlobalAcceleratorClient");
        ALL_SERVICES.put("glue","software.amazon.awssdk.services.glue.GlueClient");
        ALL_SERVICES.put("greengrass","software.amazon.awssdk.services.greengrass.GreengrassClient");
        ALL_SERVICES.put("groundstation","software.amazon.awssdk.services.groundstation.GroundStationClient");
        ALL_SERVICES.put("guardduty","software.amazon.awssdk.services.guardduty.GuardDutyClient");
        ALL_SERVICES.put("health","software.amazon.awssdk.services.health.HealthClient");
        ALL_SERVICES.put("iam","software.amazon.awssdk.services.iam.IamClient");
        ALL_SERVICES.put("inspector","software.amazon.awssdk.services.inspector.InspectorClient");
        ALL_SERVICES.put("iot","software.amazon.awssdk.services.iot.IotClient");
        ALL_SERVICES.put("iot1clickdevices","software.amazon.awssdk.services.iot1clickdevices.Iot1ClickDevicesClient");
        ALL_SERVICES.put("iot1clickprojects","software.amazon.awssdk.services.iot1clickprojects.Iot1ClickProjectsClient");
        ALL_SERVICES.put("iotanalytics","software.amazon.awssdk.services.iotanalytics.IoTAnalyticsClient");
        ALL_SERVICES.put("iotdataplane","software.amazon.awssdk.services.iotdataplane.IotDataPlaneClient");
        ALL_SERVICES.put("iotevents","software.amazon.awssdk.services.iotevents.IotEventsClient");
        ALL_SERVICES.put("ioteventsdata","software.amazon.awssdk.services.ioteventsdata.IotEventsDataClient");
        ALL_SERVICES.put("iotjobsdataplane","software.amazon.awssdk.services.iotjobsdataplane.IotJobsDataPlaneClient");
        ALL_SERVICES.put("iotthingsgraph","software.amazon.awssdk.services.iotthingsgraph.IoTThingsGraphClient");
        ALL_SERVICES.put("kafka","software.amazon.awssdk.services.kafka.KafkaClient");
        ALL_SERVICES.put("kinesis","software.amazon.awssdk.services.kinesis.KinesisClient");
        ALL_SERVICES.put("kinesisanalytics","software.amazon.awssdk.services.kinesisanalytics.KinesisAnalyticsClient");
        ALL_SERVICES.put("kinesisanalyticsv2","software.amazon.awssdk.services.kinesisanalyticsv2.KinesisAnalyticsV2Client");
        ALL_SERVICES.put("kinesisvideo","software.amazon.awssdk.services.kinesisvideo.KinesisVideoClient");
        ALL_SERVICES.put("kinesisvideoarchivedmedia","software.amazon.awssdk.services.kinesisvideoarchivedmedia.KinesisVideoArchivedMediaClient");
        ALL_SERVICES.put("kinesisvideomedia","software.amazon.awssdk.services.kinesisvideomedia.KinesisVideoMediaClient");
        ALL_SERVICES.put("kms","software.amazon.awssdk.services.kms.KmsClient");
        ALL_SERVICES.put("lambda","software.amazon.awssdk.services.lambda.LambdaClient");
        ALL_SERVICES.put("lexmodelbuilding","software.amazon.awssdk.services.lexmodelbuilding.LexModelBuildingClient");
        ALL_SERVICES.put("lexruntime","software.amazon.awssdk.services.lexruntime.LexRuntimeClient");
        ALL_SERVICES.put("licensemanager","software.amazon.awssdk.services.licensemanager.LicenseManagerClient");
        ALL_SERVICES.put("lightsail","software.amazon.awssdk.services.lightsail.LightsailClient");
        ALL_SERVICES.put("machinelearning","software.amazon.awssdk.services.machinelearning.MachineLearningClient");
        ALL_SERVICES.put("macie","software.amazon.awssdk.services.macie.MacieClient");
        ALL_SERVICES.put("managedblockchain","software.amazon.awssdk.services.managedblockchain.ManagedBlockchainClient");
        ALL_SERVICES.put("marketplacecommerceanalytics","software.amazon.awssdk.services.marketplacecommerceanalytics.MarketplaceCommerceAnalyticsClient");
        ALL_SERVICES.put("marketplaceentitlement","software.amazon.awssdk.services.marketplaceentitlement.MarketplaceEntitlementClient");
        ALL_SERVICES.put("marketplacemetering","software.amazon.awssdk.services.marketplacemetering.MarketplaceMeteringClient");
        ALL_SERVICES.put("mediaconnect","software.amazon.awssdk.services.mediaconnect.MediaConnectClient");
        ALL_SERVICES.put("mediaconvert","software.amazon.awssdk.services.mediaconvert.MediaConvertClient");
        ALL_SERVICES.put("medialive","software.amazon.awssdk.services.medialive.MediaLiveClient");
        ALL_SERVICES.put("mediapackage","software.amazon.awssdk.services.mediapackage.MediaPackageClient");
        ALL_SERVICES.put("mediapackagevod","software.amazon.awssdk.services.mediapackagevod.MediaPackageVodClient");
        ALL_SERVICES.put("mediastore","software.amazon.awssdk.services.mediastore.MediaStoreClient");
        ALL_SERVICES.put("mediastoredata","software.amazon.awssdk.services.mediastoredata.MediaStoreDataClient");
        ALL_SERVICES.put("mediatailor","software.amazon.awssdk.services.mediatailor.MediaTailorClient");
        ALL_SERVICES.put("migrationhub","software.amazon.awssdk.services.migrationhub.MigrationHubClient");
        ALL_SERVICES.put("mobile","software.amazon.awssdk.services.mobile.MobileClient");
        ALL_SERVICES.put("mq","software.amazon.awssdk.services.mq.MqClient");
        ALL_SERVICES.put("mturk","software.amazon.awssdk.services.mturk.MTurkClient");
        ALL_SERVICES.put("neptune","software.amazon.awssdk.services.neptune.NeptuneClient");
        ALL_SERVICES.put("opsworks","software.amazon.awssdk.services.opsworks.OpsWorksClient");
        ALL_SERVICES.put("opsworkscm","software.amazon.awssdk.services.opsworkscm.OpsWorksCmClient");
        ALL_SERVICES.put("organizations","software.amazon.awssdk.services.organizations.OrganizationsClient");
        ALL_SERVICES.put("personalize","software.amazon.awssdk.services.personalize.PersonalizeClient");
        ALL_SERVICES.put("personalizeevents","software.amazon.awssdk.services.personalizeevents.PersonalizeEventsClient");
        ALL_SERVICES.put("personalizeruntime","software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient");
        ALL_SERVICES.put("pi","software.amazon.awssdk.services.pi.PiClient");
        ALL_SERVICES.put("pinpoint","software.amazon.awssdk.services.pinpoint.PinpointClient");
        ALL_SERVICES.put("pinpointemail","software.amazon.awssdk.services.pinpointemail.PinpointEmailClient");
        ALL_SERVICES.put("pinpointsmsvoice","software.amazon.awssdk.services.pinpointsmsvoice.PinpointSmsVoiceClient");
        ALL_SERVICES.put("polly","software.amazon.awssdk.services.polly.PollyClient");
        ALL_SERVICES.put("pricing","software.amazon.awssdk.services.pricing.PricingClient");
        ALL_SERVICES.put("quicksight","software.amazon.awssdk.services.quicksight.QuickSightClient");
        ALL_SERVICES.put("ram","software.amazon.awssdk.services.ram.RamClient");
        ALL_SERVICES.put("rds","software.amazon.awssdk.services.rds.RdsClient");
        ALL_SERVICES.put("rdsdata","software.amazon.awssdk.services.rdsdata.RdsDataClient");
        ALL_SERVICES.put("redshift","software.amazon.awssdk.services.redshift.RedshiftClient");
        ALL_SERVICES.put("rekognition","software.amazon.awssdk.services.rekognition.RekognitionClient");
        ALL_SERVICES.put("resourcegroups","software.amazon.awssdk.services.resourcegroups.ResourceGroupsClient");
        ALL_SERVICES.put("resourcegroupstaggingapi","software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient");
        ALL_SERVICES.put("robomaker","software.amazon.awssdk.services.robomaker.RoboMakerClient");
        ALL_SERVICES.put("route53","software.amazon.awssdk.services.route53.Route53Client");
        ALL_SERVICES.put("route53domains","software.amazon.awssdk.services.route53domains.Route53DomainsClient");
        ALL_SERVICES.put("route53resolver","software.amazon.awssdk.services.route53resolver.Route53ResolverClient");
        ALL_SERVICES.put("s3","software.amazon.awssdk.services.s3.S3Client");
        ALL_SERVICES.put("sagemaker","software.amazon.awssdk.services.sagemaker.SageMakerClient");
        ALL_SERVICES.put("sagemakerruntime","software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient");
        ALL_SERVICES.put("secretsmanager","software.amazon.awssdk.services.secretsmanager.SecretsManagerClient");
        ALL_SERVICES.put("securityhub","software.amazon.awssdk.services.securityhub.SecurityHubClient");
        ALL_SERVICES.put("serverlessapplicationrepository","software.amazon.awssdk.services.serverlessapplicationrepository.ServerlessApplicationRepositoryClient");
        ALL_SERVICES.put("servicecatalog","software.amazon.awssdk.services.servicecatalog.ServiceCatalogClient");
        ALL_SERVICES.put("servicediscovery","software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient");
        ALL_SERVICES.put("ses","software.amazon.awssdk.services.ses.SesClient");
        ALL_SERVICES.put("sfn","software.amazon.awssdk.services.sfn.SfnClient");
        ALL_SERVICES.put("shield","software.amazon.awssdk.services.shield.ShieldClient");
        ALL_SERVICES.put("signer","software.amazon.awssdk.services.signer.SignerClient");
        ALL_SERVICES.put("sms","software.amazon.awssdk.services.sms.SmsClient");
        ALL_SERVICES.put("snowball","software.amazon.awssdk.services.snowball.SnowballClient");
        ALL_SERVICES.put("sns","software.amazon.awssdk.services.sns.SnsClient");
        ALL_SERVICES.put("sqs","software.amazon.awssdk.services.sqs.SqsClient");
        ALL_SERVICES.put("ssm","software.amazon.awssdk.services.ssm.SsmClient");
        ALL_SERVICES.put("storagegateway","software.amazon.awssdk.services.storagegateway.StorageGatewayClient");
        ALL_SERVICES.put("sts","software.amazon.awssdk.services.sts.StsClient");
        ALL_SERVICES.put("support","software.amazon.awssdk.services.support.SupportClient");
        ALL_SERVICES.put("swf","software.amazon.awssdk.services.swf.SwfClient");
        ALL_SERVICES.put("textract","software.amazon.awssdk.services.textract.TextractClient");
        ALL_SERVICES.put("transcribe","software.amazon.awssdk.services.transcribe.TranscribeClient");
        ALL_SERVICES.put("transcribestreaming","null");
        ALL_SERVICES.put("transfer","software.amazon.awssdk.services.transfer.TransferClient");
        ALL_SERVICES.put("translate","software.amazon.awssdk.services.translate.TranslateClient");
        ALL_SERVICES.put("waf","software.amazon.awssdk.services.waf.WafClient");
        ALL_SERVICES.put("workdocs","software.amazon.awssdk.services.workdocs.WorkDocsClient");
        ALL_SERVICES.put("worklink","software.amazon.awssdk.services.worklink.WorkLinkClient");
        ALL_SERVICES.put("workmail","software.amazon.awssdk.services.workmail.WorkMailClient");
        ALL_SERVICES.put("workspaces","software.amazon.awssdk.services.workspaces.WorkSpacesClient");
        ALL_SERVICES.put("xray","software.amazon.awssdk.services.xray.XRayClient");
    }
    //// Put in bglutil.conf.Clients, END:2.5.69

    /**
     * Empty constructor new instance.
     */
    private static Object newInstanceByConstructor(String className) throws Exception{
		Class<?> clazz = Class.forName(className);
		Constructor<?> conztructor = clazz.getConstructor();
		return conztructor.newInstance();
	}

    /**
     * Get the SDK Client dynamically, by service class name.
     */
    @SuppressWarnings("unchecked")
    public static SdkClient getClient(String className, AwsCredentialsProviderChain pcp, Region region) throws Exception {
        Region regionOri = region;
        Class<? extends SdkClient> clazz =  (Class<? extends SdkClient>) Class.forName(className);
        SdkClientBuilder<?,?> builder = (SdkClientBuilder<?,?>) clazz.getMethod("builder", new Class<?>[]{}).invoke(new Class<?>[]{}, new Object[]{});
        // Set credentials.
        Clients.invokeBuilderMethod(builder, "credentialsProvider", new Class<?>[]{AwsCredentialsProvider.class}, new Object[]{pcp});
        /// Global services endpoint and region override.
        if(className.endsWith("IamClient")){
            URI endpointNew = null;
            Region regionNew = null;
            if(General.CHINA_CS_REGIONS.containsValue(regionOri)){
                endpointNew = new URI("https://iam.cn-north-1.amazonaws.com.cn");
                regionNew = Region.of("cn-north-1");
            }
            else if(General.GLOBAL_CS_REGIONS.containsValue(regionOri)){
                endpointNew = new URI("https://iam.amazonaws.com");
                regionNew = Region.of("us-east-1");
            }
            // Set region first
            Clients.invokeBuilderMethod(builder, "region", new Class<?>[]{Region.class}, new Object[]{regionNew});
            // Set endpoint after
            Clients.invokeBuilderMethod(builder, "endpointOverride", new Class<?>[]{URI.class}, new Object[]{endpointNew});
        }
        else{
            // Set region.
            Clients.invokeBuilderMethod(builder, "region", new Class<?>[]{Region.class}, new Object[]{regionOri});
        }
        return (SdkClient) builder.build();
    }

    /**
     * Get the SDK Client dynamically, by service class name.
     */
    public static SdkClient getClientByServiceClass(String className, String profile) throws Exception {
        AwsCredentialsProviderChain cred = AccessKeys.getCpcByProfile(profile);
        Region region = General.PROFILE_REGION.get(profile);
        return Clients.getClient(className,cred,region);
    }

    /**
     * Invoke builder method.
     */
    public static void invokeBuilderMethod(SdkClientBuilder<?,?> builder, String methodName, Class<?>[] paramClasses, Object[] paramObjects) throws Exception{
        Method m = builder.getClass().getMethod(methodName, paramClasses);
        m.invoke(builder, paramObjects);
    }

    /**
     * Get the SDK Client dynamically, by service class name.
     */
    public static SdkClient getClientByServiceName(String serviceName, String profile) throws Exception{
        String serviceClass = Clients.ALL_SERVICES.get(serviceName);
        SdkClient c = null;
        if(serviceClass.equals("null")){
            return c;
        }
        return Clients.getClientByServiceClass(serviceClass, profile);
    }

    /**
     * Get the IUtil instance from service name.
     */
    public static CUtil getCUtilByServiceName(String serviceName) throws Exception{
        CUtil cu = null;
        String cUtilPrefix = "awsviewer.common.U";
        cu = (CUtil) newInstanceByConstructor(cUtilPrefix+serviceName);
        return cu;
    }
}

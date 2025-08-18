# vpc 모듈: 퍼블릭 및 프라이빗 서브넷을 포함한 VPC 생성
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "~>6.0"

  name = "${var.cluster_base_name}-VPC"
  cidr = var.vpc_block
  azs = var.availability_zone

  enable_dns_support   = true
  enable_dns_hostnames = true

  public_subnets  = var.public_subnet_blocks
  private_subnets = var.private_subnet_blocks

  # NAT Gateway 활성화해서 Private Subnet의 외부 통신을 가능하도록 구성
  enable_nat_gateway = true
  single_nat_gateway = true
  one_nat_gateway_per_az = false
  manage_default_network_acl = false

  map_public_ip_on_launch = true

  igw_tags = {
    "Name" = "${var.cluster_base_name}-IGW"
  }

  nat_gateway_tags = {
    "Name" = "${var.cluster_base_name}-NAT"
  }

  public_subnet_tags = {
    "Name"                     = "${var.cluster_base_name}-PublicSubnet"
    "kubernetes.io/role/elb"   = "1"
  }

  private_subnet_tags = {
    "Name"                             = "${var.cluster_base_name}-PrivateSubnet"
    "kubernetes.io/role/internal-elb" = "1"
  }

  tags = {
    "Environment" = "prod"
  }
}

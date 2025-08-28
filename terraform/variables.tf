variable "key_name" {
  description = "SSH 접근을 위한 EC2 키페어 이름"
  type        = string
}

variable "my_iam_user_access_key_id" {
  description = "IAM User - Access Key ID"
  type        = string
  sensitive   = true
}

variable "my_iam_user_secret_access_key" {
  default   = "IAM User - AWS Secret Access Key"
  type      = string
  sensitive = true
}

variable "sg_ingress_ssh_cidr" {
  description = "SSH 접근을 허용할 CIDR 형식의 IP 대역"
  type        = string
  validation {
    condition = can(regex("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})$", var.sg_ingress_ssh_cidr))
    error_message = "sg_ingress_ssh_cidr는 x.x.x.x/x IP CIDR 범위로 할당해야 합니다."
  }
}

variable "my_instance_type" {
  description = "Bastion Host EC2 인스턴스 타입"
  type        = string
  default     = "t3.medium"
  validation {
    condition = contains(["t2.micro", "t2.small", "t2.medium", "t3.micro", "t3.small", "t3.medium"], var.my_instance_type)
    error_message = "t2.micro, t2.small, t2.medium, t3.micro, t3.small, t3.medium 중 하나를 선택해주세요."
  }
}

variable "cluster_base_name" {
  description = "Cluster 이름"
  type        = string
  default     = "idolu"
}

variable "kubernetes_version" {
  description = "EKS Cluster에서 사용할 Kubernetes 버전"
  type        = string
  default     = "1.33"
}

variable "worker_node_instance_type" {
  description = "워커 노드 EC2 인스턴스 타입"
  type        = string
  default     = "t3.medium"
}

variable "worker_node_count" {
  description = "워커 그룹 내 노드 수"
  type    = string
  default = 3
}

variable "worker_node_volume_size" {
  description = "워커 노드에 연결될 EBS 볼륨 크기"
  type = number
  default = 30
}

variable "target_region" {
  description = "리소스를 생성할 AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "aws_profile" {
  description = "AWS CLI 프로파일"
  type        = string
  default     = "my-profile"
}

variable "availability_zone" {
  description = "가용 영역 리스트"
  type        = list(string)
  default     = ["ap-northeast-2a", "ap-northeast-2b", "ap-northeast-2c"]
}

variable "vpc_block" {
  description = "VPC CIDR 블록"
  type = string
  default = "192.168.0.0/16"
}

variable "public_subnet_blocks" {
  description = "public subnets CIDR blocks"
  type        = list(string)
  default     = ["192.168.1.0/24", "192.168.2.0/24", "192.168.3.0/24"]
}

variable "private_subnet_blocks" {
  description = "private subnets CID blocks"
  type = list(string)
  default = ["192.168.11.0/24", "192.168.12.0/24", "192.168.13.0/24"]
}

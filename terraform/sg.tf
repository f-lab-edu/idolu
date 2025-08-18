# 보안 그룹: Bastion Host를 위한 보안 그룹을 생성
resource "aws_security_group" "eks_sec_group" {
  vpc_id = module.vpc.vpc_id

  name        = "${var.cluster_base_name}-eks-sec-group"
  description = "Security group for ${var.cluster_base_name} Host"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.sg_ingress_ssh_cidr]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.cluster_base_name}-HOST-SG"
  }
}

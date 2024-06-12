variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "security_group_id" {
  description = "Security Group ID"
  type        = string
}

variable "vpc_subnets" {
  description = "Subnets for the VPC"
  type = list(string)
}
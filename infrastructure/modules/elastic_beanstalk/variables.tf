variable "app_name" {
  type = string
  description = "The name of the application"
}


variable "app_vpc_id" {
  type = string
  description = "The VPC ID for the application"
}

variable "app_subnet_id" {
  type = string
  description = "The subnet ID for the application"
}

variable "app_security_group_id" {
  type = string
  description = "The security group ID for the application"
}

variable "key_name" {
  type = string
  description = "The key name for the application"
}
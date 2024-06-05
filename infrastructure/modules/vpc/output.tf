output "app_vpc_id" {
  value = aws_vpc.app_vpc.id
}

output "app_subnets" {
  value = [aws_subnet.app_subnet_1.id, aws_subnet.app_subnet_2.id]
}

output "app_security_group_id" {
  value = aws_security_group.app_sg.id
}
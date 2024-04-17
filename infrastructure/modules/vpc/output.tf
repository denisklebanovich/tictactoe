
output "app_vpc_id" {
  value = aws_vpc.app_vpc.id
}

output "app_subnet_id" {
  value = aws_subnet.app_subnet.id
}

output "app_security_group_id" {
  value = aws_security_group.app_sg.id
}
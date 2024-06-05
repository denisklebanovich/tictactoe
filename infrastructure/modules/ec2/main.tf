resource "aws_launch_template" "app_template" {
  name          = "app-template"
  image_id      = var.ami
  instance_type = "t2.micro"
  key_name      = var.key_name

  network_interfaces {
    security_groups             = [var.app_security_group_id]
    associate_public_ip_address = true
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "tictactoe"
    }
  }

  user_data = filebase64("./setup.sh")
}

resource "aws_security_group" "lb_sg" {
  name        = "lb-security-group"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb" "app_lb" {
  name                       = "app-lb"
  internal                   = false
  load_balancer_type         = "application"
  security_groups            = [aws_security_group.lb_sg.id]
  subnets                    = var.app_subnets
  enable_deletion_protection = false
}

resource "aws_lb_target_group" "app_tg" {
  name     = "app-tg"
  port     = 5173
  protocol = "HTTP"
  vpc_id   = var.vpc_id

  tags = {
    Name = "app-tg"
  }
}


resource "aws_lb_listener" "app_lb_listener" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

resource "aws_autoscaling_group" "app_asg" {
  desired_capacity = 0
  max_size         = 3
  min_size         = 1
  launch_template {
    id      = aws_launch_template.app_template.id
    version = "$Latest"
  }
  target_group_arns   = [aws_lb_target_group.app_tg.arn]
  vpc_zone_identifier = var.app_subnets

  tag {
    key                 = "Name"
    value               = "tictactoe"
    propagate_at_launch = true
  }

  enabled_metrics = ["GroupInServiceInstances"]
}

resource "aws_autoscaling_policy" "scale_up" {
  name                   = "scale-up"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.app_asg.name
}

resource "aws_autoscaling_policy" "scale_down" {
  name                   = "scale-down"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.app_asg.name
}

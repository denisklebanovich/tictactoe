data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

resource "aws_ecs_cluster" "app_cluster" {
  name = "${var.app_name}-cluster"
}

resource "aws_ecs_service" "app_service" {
  name            = "${var.app_name}-service"
  cluster         = aws_ecs_cluster.app_cluster.id
  task_definition = aws_ecs_task_definition.app_task_definition.arn
  desired_count   = 1
  launch_type = "FARGATE"

  network_configuration {
    subnets          = [var.app_subnet_id]
    security_groups  = [var.app_security_group_id]
    assign_public_ip = true
  }
}

resource "aws_ecs_task_definition" "app_task_definition" {
  family                   = "${var.app_name}-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  cpu                      = "256"
  memory                   = "512"
  container_definitions    = jsonencode([
    {
      name         = "backend"
      image        = "gguser1/tictactoe-backend"
      essential    = true
      portMappings = [
        {
          containerPort = 8080,
          hostPort      = 8080,
          protocol      = "tcp"
        }
      ]
    },
    {
      name         = "frontend"
      image        = "gguser1/tictactoe-frontend:fargate"
      essential    = true
      portMappings = [
        {
          containerPort = 5173,
          hostPort      = 5173,
          protocol      = "tcp"
        }
      ],
      dependsOn = [
        {
          containerName = "backend",
          condition     = "START"
        }
      ]
    }
  ])
}
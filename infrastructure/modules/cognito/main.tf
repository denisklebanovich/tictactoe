resource "aws_cognito_user_pool" "user_pool" {
  name                     = "user-pool"
  alias_attributes         = ["email"]
  auto_verified_attributes = ["email"]
}

resource "aws_cognito_user_pool_client" "user_pool_client" {
  name = "app-client"

  user_pool_id        = aws_cognito_user_pool.user_pool.id
  generate_secret     = false
  explicit_auth_flows = ["ALLOW_REFRESH_TOKEN_AUTH", "ALLOW_USER_PASSWORD_AUTH", "ALLOW_ADMIN_USER_PASSWORD_AUTH"]
}

resource "aws_cognito_user_pool_domain" "user_pool_domain" {
  domain       = "dklebanovich"
  user_pool_id = aws_cognito_user_pool.user_pool.id
}
/**
 * Copyright 2014 Marco Vermeulen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.gvmtool.release.conf

import org.springframework.beans.factory.annotation.{Autowired, Qualifier, Value}
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration._
import org.springframework.security.oauth2.config.annotation.web.configurers.{AuthorizationServerEndpointsConfigurer, ResourceServerSecurityConfigurer}
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  val resourceId = "rest-service"
  val releaseRegex = "/release.*"
  val adminRegex = "/admin/((?!health|info).)*"

  override def configure(httpSecurity: HttpSecurity) =
    httpSecurity.authorizeRequests()
      .regexMatchers(HttpMethod.POST, releaseRegex).authenticated()
      .regexMatchers(HttpMethod.GET, adminRegex).authenticated()

  override def configure(resources: ResourceServerSecurityConfigurer) = resources.resourceId(resourceId)

}

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  val grantType = "password"
  val authority = "USER"
  val clientScopes = Array("read", "write")
  val resourceId = "rest-service"

  @Value("#{systemEnvironment['CLIENT_ID']}")
  var clientId = "client_id"

  @Value("#{systemEnvironment['CLIENT_SECRET']}")
  var clientSecret = "client_secret"

  val tokenStore = new InMemoryTokenStore

  @Autowired
  @Qualifier("authenticationManagerBean")
  var authenticationManager: AuthenticationManager = null

  override def configure(endpoints: AuthorizationServerEndpointsConfigurer) =
    endpoints
      .tokenStore(tokenStore)
      .authenticationManager(authenticationManager)

  override def configure(clients: ClientDetailsServiceConfigurer) =
    clients
      .inMemory()
      .withClient(clientId)
      .secret(clientSecret)
      .authorizedGrantTypes(grantType)
      .authorities(authority)
      .scopes(clientScopes: _*)
      .resourceIds(resourceId)

}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  val role = "USER"

  @Value("#{systemEnvironment['AUTH_USERNAME']}")
  var authUsername = "auth_username"

  @Value("#{systemEnvironment['AUTH_PASSWORD']}")
  var authPassword = "auth_password"

  override def configure(auth: AuthenticationManagerBuilder) =
    auth
      .inMemoryAuthentication()
      .withUser(authUsername)
      .password(authPassword)
      .roles(role)

  @Bean
  override def authenticationManagerBean = super.authenticationManagerBean

}
package com.waruna.authtest;

/**
 * Created by waruna on 3/1/16.
 */
import com.waruna.authtest.dao.JdbcUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.inject.Inject;
import javax.sql.DataSource;


@Configuration
public class OAuth2ServerConfiguration  {

    private static final String RESOURCE_ID = "restservice";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            // @formatter:off
            resources
                    .resourceId(RESOURCE_ID);
            // @formatter:on
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                    .antMatchers("/users").hasRole("ADMIN")
                    .antMatchers("/greeting").authenticated()
                    .antMatchers("/categories").authenticated()
                    .antMatchers("/feeds").authenticated()
                    .antMatchers("/interest").authenticated()
                    .antMatchers("/interest/add").authenticated()
                    .antMatchers("/locations").authenticated();
            // @formatter:on
        }

    }

    @Configuration
    @EnableAuthorizationServer
        protected static class AuthorizationServerConfiguration extends
            AuthorizationServerConfigurerAdapter {

        @Autowired
        private DataSource dataSource;// = DataSourceBuilder.create()
//                .driverClassName("com.mysql.jdbc.Driver")
//                .url("jdbc:mysql://127.0.0.1:8889/curve").username("curve").password("curve").build();

        private TokenStore tokenStore;// = new JdbcTokenStore(dataSource);

//        @Autowired
//        public void setDataSource(DataSource dataSource) {
//            this.dataSource = dataSource;
//        }

        //@Autowired
        //@Qualifier("authenticationManagerBean")
        @Inject
        private AuthenticationManager authenticationManager;

        @Autowired
        private JdbcUserDAO userDetailsService;

        @Bean
        public JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            // @formatter:off
            endpoints.userDetailsService(userDetailsService)
                    .authorizationCodeServices(authorizationCodeServices())
                    .authenticationManager(this.authenticationManager)
                    .tokenStore(tokenStore())
                    .approvalStoreDisabled();
            // @formatter:on
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            // @formatter:off
            clients.jdbc(dataSource)
                    .withClient("clientapp")
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("USER")
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID)
                    .secret("123456");
            // @formatter:on
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(tokenStore());
            return tokenServices;
        }

    }

}

package com.example.demo.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.example.demo.domain.Module;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	
    @Lazy
	@Autowired
    private UserService userService;
    
    //认证.登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	    
    	    String username = (String) token.getPrincipal();
    	    User user = userService.findUserByUserName(username);
    	    if(user==null){
    	      //木有找到用户
    	      throw new UnknownAccountException("没有找到该账号");
    	    }
    	    /**
    	     * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现  
    	     */
    	    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),getName());
    	    // 当验证都通过后，把用户信息放在session里
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user.getUsername());
            Role role = (Role) new ArrayList(user.getRoles()).get(0);
            session.setAttribute("role", role.getRname());
    	    return info;
    	  }
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
    	 SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
    	String username = (String)super.getAvailablePrincipal(principal); 
    	User user = userService.findUserByUserName(username);
    	//权限名的集合
        Set<String> permissions = new HashSet<String>();
        //角色名的集合
        Set<String> roleSet = new HashSet<String>();
        
        Set<Role> roles = user.getRoles();
        if(roles.size()>0) {
            for(Role role : roles) {
            	roleSet.add(role.getRname());
                Set<Module> modules = role.getModules();
                if(modules.size()>0) {
                    for(Module module : modules) {
                    	String  permission=module.getMname();
                    	if(permission.indexOf("perms")!=-1)
                    	permissions.add(permission.substring(permission.indexOf("perms")+6, permission.length()-1));
                    }
                }
            }
        }
        info.addRoles(roleSet);
        info.addStringPermissions(permissions);
        
        return info;
    }
    
    @Override
    public String getName() {
      return getClass().getName();
    }

}

package com.dms.api.modules.controller.sys.user;

import com.dms.api.common.utils.R;
import com.dms.api.common.utils.RedisService;
import com.dms.api.common.utils.ShiroUtils;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.sys.user.SysUserService;
import com.dms.api.modules.service.sys.user.SysUserTokenService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 */
@RestController
public class SysLoginController extends AbstractController {

	@Autowired
	private Producer producer;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private RedisService redisService;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha)throws IOException {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		redisService.delete(ConstantTable.USER_INFO_REDIS_KEY_PREFIX+user.getUserId());
		if(user.isSysSuperAdmin()){
			r.put("adminEd",true);
		}else {
			r.put("adminEd",false);
		}

		return r;
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}

	@RequestMapping(value = "/sys/isOnline", method = RequestMethod.POST)
	public R isOnline(String token) {

		try {
			if(StringUtils.isEmpty(token)){
                return R.error("token不能为空");
            }

			boolean online = sysUserTokenService.isOnline(token);

			Map result = new HashMap();
			result.put("isOnline",online);
			return R.ok().put("data",result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return R.error();
	}
	
}

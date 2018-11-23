package org.spring.springboot.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.config.WxMaConfiguration;
import org.spring.springboot.domain.MiniUser;
import org.spring.springboot.service.MiniUserService;
import org.spring.springboot.utils.JsonUtils;
import org.spring.springboot.utils.ResponseBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Date;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
public class WxMaUserController {

    @Autowired
    private MiniUserService miniUserService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 登陆接口
     */
    @GetMapping("/login/{appid}/{code}")
    public ResponseBO login(@PathVariable String appid, @PathVariable String code) {
        ResponseBO bo = new ResponseBO();
        if (StringUtils.isBlank(code)) {
            return bo.completeFail("code为空!");
        }

        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {

            return bo.completeFail("未找到对应appid的配置，请核实！"+appid);
        }

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid" , 0);
            jsonObject.put("openId" , session.getOpenid());
            jsonObject.put("sessionKey",session.getSessionKey());
            MiniUser miniUser = miniUserService.getByOpenId(session.getOpenid());
            if (null == miniUser){
                miniUser = new MiniUser();
                Date now = new Date();
                if(session.getUnionid()!=null){
                    miniUser.setUnionId(session.getUnionid());
                }
                miniUser.setOpenId(session.getOpenid());
                miniUser.setCreateTime(now);
                miniUser.setUpdateTime(now);
                miniUserService.save(miniUser);
                bo.completeOK();
                bo.setData(jsonObject);
            }
            //TODO 可以增加自己的逻辑，关联业务相关数据
            return bo;
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return bo.completeFail("loginFail!");
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

}

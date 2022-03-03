package cn.hibit.module.member.convert.auth;

import cn.hibit.framework.common.enums.UserTypeEnum;
import cn.hibit.framework.security.core.LoginUser;
import cn.hibit.module.member.dal.dataobject.user.MemberUserDO;
import cn.hibit.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.hibit.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.hibit.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.hibit.module.system.api.social.dto.SocialUserUnbindReqDTO;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.*;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    @Mapping(source = "mobile", target = "username")
    LoginUser convert0(MemberUserDO bean);

    default LoginUser convert(MemberUserDO bean) {
        // 目的，为了设置 UserTypeEnum.MEMBER.getValue()
        return convert0(bean).setUserType(UserTypeEnum.MEMBER.getValue());
    }

    SocialUserBindReqDTO convert(Long userId, Integer userType, AppAuthSocialBindReqVO reqVO);

    SocialUserBindReqDTO convert(Long userId, Integer userType, AppAuthSocialLogin2ReqVO reqVO);

    SocialUserBindReqDTO convert(Long userId, Integer userType, AppAuthSocialLoginReqVO reqVO);

    SocialUserUnbindReqDTO convert(Long userId, Integer userType, AppAuthSocialUnbindReqVO reqVO);

    SmsCodeSendReqDTO convert(AppAuthSendSmsReqVO reqVO);

    SmsCodeUseReqDTO convert(AppAuthResetPasswordReqVO reqVO, SmsSceneEnum scene, String usedIp);

    SmsCodeUseReqDTO convert(AppAuthSmsLoginReqVO reqVO, Integer scene, String userIp);

}

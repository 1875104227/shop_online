package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.common.exception.ServerException;
import cn.niit.shop_online.common.utils.AliyunResource;
import cn.niit.shop_online.common.utils.FileResource;
import cn.niit.shop_online.common.utils.GeneratorCodeUtilsn;
import cn.niit.shop_online.common.utils.JWTUtils;
import cn.niit.shop_online.convert.UserConvert;
import cn.niit.shop_online.entity.User;
import cn.niit.shop_online.mapper.UserMapper;
import cn.niit.shop_online.query.UserLoginQuery;
import cn.niit.shop_online.service.RedisService;
import cn.niit.shop_online.service.UserService;
import cn.niit.shop_online.vo.LoginResultVO;
import cn.niit.shop_online.vo.UserTokenVO;
import cn.niit.shop_online.vo.UserVO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static cn.niit.shop_online.constant.APIConstant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisService redisService;

    private  final FileResource fileResource;

    private  final AliyunResource aliyunResource;

    @Override
    public LoginResultVO login(UserLoginQuery query) {
        //  1、获取openId
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&js_code=" + query.getCode() +
                "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String openIdResult = restTemplate.getForObject(url, String.class);
        if (StringUtils.contains(openIdResult, WX_ERR_CODE)) {
            throw new ServerException("openId获取失败" + openIdResult);
        }
        JSONObject jsonObject = JSON.parseObject(openIdResult);
        String openId = jsonObject.getString(WX_OPENID);
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        if (user == null) {
            user = new User();
            String account = "用户" + GeneratorCodeUtilsn.generateCode();
            user.setAvatar(DEFAULT_AVATAR);
            user.setAccount(account);
            user.setNickname(account);
            user.setOpenId(openId);
            user.setMobile("''");
            baseMapper.insert(user);
        }
        LoginResultVO userVO = UserConvert.INSTANCE.convertToLoginResultVO(user);
//4、生成token,存入redis并设置过期时间
        UserTokenVO tokenVO = new UserTokenVO( userVO.getId());
        String token = JWTUtils.generateToken(JWT_SECRET,tokenVO.toMap());
        redisService.set(APP_NAME + userVO.getId(),token,APP_TOKEN_EXPIRE_TIME);
        userVO.setToken(token);
        return userVO;


    }

    @Override
    public User getUserInfo(Integer userId) {
        User user = baseMapper.selectById(userId);
        if(user==null){
            throw new ServerException("用户不存在");
        }
        return user;
    }

    @Override
    public UserVO editUserInfo(UserVO userVO) {
        User user =baseMapper.selectById(userVO.getId());
        if (user==null){
            throw new ServerException("用户不存在");
        }
        User userConvert= UserConvert.INSTANCE.convert(userVO);
        updateById(userConvert);
        return userVO;
    }

    @Override
    public String editUserAvatar(Integer userId, MultipartFile file) {
        //读入配置信息
        String endpoint=fileResource.getEndpoint();
        String accessKeyId=aliyunResource.getAccessKeyId();
        String accessKeySecret=aliyunResource.getAccessKeySecret();

        //创建osclient实例
        OSS ossClient =new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        String filename=file.getOriginalFilename();

        //分割文件名，获取文件后缀名
        assert  filename !=null;
        String[] fileNameArr=filename.split("\\.");
        String suffix=fileNameArr[fileNameArr.length -1];
        //拼接得到新的上传文件名
        String uploadFileName=fileResource.getBucketName()+ UUID.randomUUID()+"."+suffix;
        //上传网络需要用的字节流
        InputStream inputStream=null;
        try {
            inputStream=file.getInputStream();
        } catch (IOException e) {
            throw new ServerException("文件上次失败");
        }

        //执行阿里云上传操作
        ossClient.putObject(fileResource.getBucketName(),uploadFileName,inputStream);
        //关闭OSSClient
        ossClient.shutdown();

        //修改用户头像
        User user=baseMapper.selectById(userId);
        if (user ==null){
            throw  new ServerException("用户不存在");
        }
        uploadFileName = fileResource.getOssHost()+uploadFileName;
        user.setAvatar(uploadFileName);
        baseMapper. updateById(user);
        return uploadFileName;



    }
}

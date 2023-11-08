package cn.niit.shop_online.service.impl;

import cn.niit.shop_online.entity.Category;
import cn.niit.shop_online.mapper.CategoryMapper;
import cn.niit.shop_online.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}

package cn.niit.shop_online.controller;

import cn.niit.shop_online.common.result.Result;
import cn.niit.shop_online.query.CartQuery;
import cn.niit.shop_online.service.UserShoppingCartService;
import cn.niit.shop_online.vo.CartGoodsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.niit.shop_online.common.utils.ObtainUserIdUtils.getUserId;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Tag(name = "购物车管理")
@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class UserShoppingCartController {
    private final UserShoppingCartService userShoppingCartService;

    @Operation(summary = "加入购物车")
    @PostMapping("add")
    public Result<CartGoodsVO> addShopCart(@RequestBody @Validated CartQuery query, HttpServletRequest request) {
        query.setUserId(getUserId(request));
        CartGoodsVO goodsVO = userShoppingCartService.addShopCart(query);
        return Result.ok(goodsVO);
    }

}

package cn.niit.shop_online.controller;

import cn.niit.shop_online.common.result.PageResult;
import cn.niit.shop_online.common.result.Result;
import cn.niit.shop_online.query.RecommendByTabGoodsQuery;
import cn.niit.shop_online.service.GoodsService;
import cn.niit.shop_online.vo.GoodsVO;
import cn.niit.shop_online.vo.IndexTabRecommendVO;
import cn.niit.shop_online.vo.RecommendGoodsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yaaii
 * @since 2023-11-07
 */
@Tag(name = "商品模块")
@RestController
@RequestMapping("goods")
@AllArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @Operation(summary = "首页-热门推荐商品列表")
    @PostMapping("preference")
    public Result<IndexTabRecommendVO> getTabRecommendGoodsByTabId(@RequestBody @Validated RecommendByTabGoodsQuery query) {
        IndexTabRecommendVO result = goodsService.getTabRecommendGoodsByTabId(query);
        return Result.ok(result);
    }
    @Operation(summary = "首页-猜你喜欢")
    @PostMapping("guessLike")
    public Result<PageResult<RecommendGoodsVO>> getRecommendGoodsByPage(@RequestBody @Validated RecommendByTabGoodsQuery query) {
        PageResult<RecommendGoodsVO> result = goodsService.getRecommendGoodsByPage(query);
        return Result.ok(result);
    }
    @Operation(summary = "首页-商品详情")
    @GetMapping("detail")
    public Result<GoodsVO> getGoodsDetail(@RequestParam Integer id) {
        GoodsVO goodsDetail = goodsService.getGoodsDetail(id);
        return Result.ok(goodsDetail);
    }
}

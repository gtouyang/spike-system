package com.ogic.spikesystemproductservice.mapper;

import com.ogic.spikesystemapi.entity.ProductEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品的MyBatis接口,所有与MySQL相关的操作都通过它来实现
 *
 * @author ogic
 * @date 2019-07-16
 */
@Mapper
public interface ProductMapper {

    @Select("select * from product where id = #{id}")
    public ProductEntity getProductById(Long id);

    @Select("select * from product order by id desc")
    public List<ProductEntity> getAllProductsOrderByCreateTime();

    @Select("select * from product order by id desc limit #{offset} #{rows}")
    public List<ProductEntity> getProducts(Integer offset, Integer rows);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into product(name, amount, origin_price, spike_price, spike_start_time, spike_end_time, image_url, info)" +
            "values(#{name}, " +
            "#{amount}, " +
            "#{originPrice}, " +
            "#{spikePrice}, " +
            "#{spikeStartTime}, " +
            "#{spikeEndTime}, " +
            "#{imageUrl}, " +
            "#{info})")
    public void insertProduct(ProductEntity product);
}

package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(int page, int rows, String search, String sortBy, Boolean descendind) {
        //开启分页
        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        //模糊查询
        if (StringUtils.isNotEmpty(search)) {
            example.createCriteria().andLike("name", "%" + search + "%")
                    .orEqualTo("letter", search.toUpperCase());
        }
        // 排序查询
        if (StringUtils.isNotEmpty(sortBy)) {
            example.setOrderByClause(sortBy + (descendind ? " DESC" : " ASC"));
        }
        Page<Brand> pages = (Page<Brand>) brandMapper.selectByExample(example);
        PageResult<Brand> pageResult = new PageResult<Brand>();
        pageResult.setTotal(pages.getTotal());
        pageResult.setItems(pages);
        pageResult.setTotalPage(new Long(pages.getPages()));
        return pageResult;
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Integer> cids) {
        brandMapper.insertSelective(brand);
        cids.forEach(cid -> {
            brandMapper.insertCategoryBrand(cid, brand.getId());
        });
    }

    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Integer> cids) {

        brandMapper.updateByPrimaryKeySelective(brand);
        brandMapper.deleteCategoryByBid(brand.getId());
        cids.forEach(cid -> {
            brandMapper.insertCategoryBrand(cid, brand.getId());
        });
    }

    @Override
    @Transactional
    public void deleteBrandById(long bid) {
        Brand brand = new Brand();
        brand.setId(bid);
        brandMapper.deleteByPrimaryKey(brand);
        brandMapper.deleteCategoryByBid(bid);

    }

    @Override
    public String selectByPrimaryKey(Long brandId) {
        Brand brand = new Brand();
        brand.setId(brandId);
        Brand bname = brandMapper.selectByPrimaryKey(brand);
        return bname.getName();
    }

    @Override
    public List<Brand> queryBrandByCategorybid(Long cid) {
        return brandMapper.queryBrandByCategorybid(cid);

    }

    @Override
        public List<Brand> queryBrandByIds(List<Long> ids) {
            return this.brandMapper.selectByIdList(ids);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
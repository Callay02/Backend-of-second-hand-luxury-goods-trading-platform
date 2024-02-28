package icu.callay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.entity.User;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.mapper.PurchaseOrderFormMapper;
import icu.callay.entity.PurchaseOrderForm;
import icu.callay.mapper.PurchaseOrderFormStateMapper;
import icu.callay.service.PurchaseOrderFormService;
import icu.callay.vo.PageVo;
import icu.callay.vo.PurchaseOrderFormPageVo;
import icu.callay.vo.PurchaseOrderFormVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (PurchaseOrderForm)表服务实现类
 *
 * @author makejava
 * @since 2024-02-24 03:08:04
 */
@Service("purchaseOrderFormService")
public class PurchaseOrderFormServiceImpl extends ServiceImpl<PurchaseOrderFormMapper, PurchaseOrderForm> implements PurchaseOrderFormService {

    @Autowired
    private PurchaseOrderFormMapper purchaseOrderFormMapper;

    @Autowired
    private PurchaseOrderFormStateMapper purchaseOrderFormStateMapper;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Override
    public SaResult createOrderForm(PurchaseOrderForm purchaseOrderForm) {
        try {
            purchaseOrderForm.setCreateTime(new Date());
            purchaseOrderForm.setUpdateTime(new Date());
            purchaseOrderForm.setState(0);
            save(purchaseOrderForm);
            return SaResult.ok("创建成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getPurchaseOrderFormPageByStateAndUid(int state,int page,int rows) {
        try {
            String uid = (String) StpUtil.getLoginId();
            Page<PurchaseOrderForm> purchaseOrderFormPage = new Page<>(page,rows);
            purchaseOrderFormMapper.selectPage(purchaseOrderFormPage,new QueryWrapper<PurchaseOrderForm>().eq("uid",uid).and(wrapper->{wrapper.eq("state",state);}));

            List<PurchaseOrderFormVo> purchaseOrderFormVoList = new ArrayList<>();

            purchaseOrderFormPage.getRecords().forEach(purchaseOrderForm -> {
                PurchaseOrderFormVo purchaseOrderFormVo = new PurchaseOrderFormVo();
                BeanUtils.copyProperties(purchaseOrderForm,purchaseOrderFormVo);

                purchaseOrderFormVo.setBrandName(goodsBrandMapper.selectById(purchaseOrderForm.getBrand()).getName());
                purchaseOrderFormVo.setTypeName(goodsTypeMapper.selectById(purchaseOrderForm.getType()).getName());

                purchaseOrderFormVoList.add(purchaseOrderFormVo);
            });

            PurchaseOrderFormPageVo purchaseOrderFormPageVo = new PurchaseOrderFormPageVo();
            purchaseOrderFormPageVo.setPurchaseOrderFormVoList(purchaseOrderFormVoList);
            purchaseOrderFormPageVo.setTotal(purchaseOrderFormPage.getTotal());

            return SaResult.data(purchaseOrderFormPageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getPurchaseOrderFormPageByState(int state, int page, int rows) {
        try {
            Page<PurchaseOrderForm> purchaseOrderFormPage = new Page<>(page,rows);
            purchaseOrderFormMapper.selectPage(purchaseOrderFormPage,new QueryWrapper<PurchaseOrderForm>().eq("state",state));

            List<PurchaseOrderFormVo> purchaseOrderFormVoList = new ArrayList<>();

            purchaseOrderFormPage.getRecords().forEach(purchaseOrderForm -> {
                PurchaseOrderFormVo purchaseOrderFormVo = new PurchaseOrderFormVo();
                BeanUtils.copyProperties(purchaseOrderForm,purchaseOrderFormVo);

                purchaseOrderFormVo.setBrandName(goodsBrandMapper.selectById(purchaseOrderForm.getBrand()).getName());
                purchaseOrderFormVo.setTypeName(goodsTypeMapper.selectById(purchaseOrderForm.getType()).getName());

                purchaseOrderFormVoList.add(purchaseOrderFormVo);
            });

            PurchaseOrderFormPageVo purchaseOrderFormPageVo = new PurchaseOrderFormPageVo();
            purchaseOrderFormPageVo.setPurchaseOrderFormVoList(purchaseOrderFormVoList);
            purchaseOrderFormPageVo.setTotal(purchaseOrderFormPage.getTotal());

            return SaResult.data(purchaseOrderFormPageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updatePurchaseOrderFormSateById(Long id) {
        try {
            PurchaseOrderForm purchaseOrderForm = getById(id);
            purchaseOrderForm.setState(1);
            purchaseOrderForm.setUpdateTime(new Date());
            update(purchaseOrderForm,new UpdateWrapper<PurchaseOrderForm>().eq("id",id));
            return SaResult.ok("签收成功");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getPurchaseOrderFormById(Long id) {
        try {
            PurchaseOrderForm purchaseOrderForm = getById(id);
            return SaResult.data(purchaseOrderForm);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateStateSet2ById(Long id) {
        try {
            PurchaseOrderForm purchaseOrderForm = getById(id);
            purchaseOrderForm.setApid((String) StpUtil.getLoginId());
            purchaseOrderForm.setState(2);
            purchaseOrderForm.setUpdateTime(new Date());
            update(purchaseOrderForm,new UpdateWrapper<PurchaseOrderForm>().eq("id",id));
            return SaResult.ok();
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult getPurchaseOrderFormPageByApid(String apid,int page,int rows) {
        try {
            if(apid==null)
                apid= (String) StpUtil.getLoginId();
            Page<PurchaseOrderForm> purchaseOrderFormPage = new Page<>(page,rows);
            purchaseOrderFormMapper.selectPage(purchaseOrderFormPage,new QueryWrapper<PurchaseOrderForm>().eq("apid",apid));

            List<PurchaseOrderFormVo> purchaseOrderFormVoList = new ArrayList<>();

            purchaseOrderFormPage.getRecords().forEach(purchaseOrderForm -> {
                PurchaseOrderFormVo purchaseOrderFormVo = new PurchaseOrderFormVo();
                BeanUtils.copyProperties(purchaseOrderForm,purchaseOrderFormVo);

                purchaseOrderFormVo.setBrandName(goodsBrandMapper.selectById(purchaseOrderForm.getBrand()).getName());
                purchaseOrderFormVo.setTypeName(goodsTypeMapper.selectById(purchaseOrderForm.getType()).getName());

                purchaseOrderFormVoList.add(purchaseOrderFormVo);
            });

            PageVo<PurchaseOrderFormVo> purchaseOrderFormPageVo = new PageVo<>();
            purchaseOrderFormPageVo.setData(purchaseOrderFormVoList);
            purchaseOrderFormPageVo.setTotal(purchaseOrderFormPage.getTotal());
            return SaResult.data(purchaseOrderFormPageVo);
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }

    @Override
    public SaResult updateStateSet3ById(PurchaseOrderForm purchaseOrderForm) {
        try {
            purchaseOrderForm.setApid((String) StpUtil.getLoginId());
            purchaseOrderForm.setState(3);
            purchaseOrderForm.setUpdateTime(new Date());
            update(purchaseOrderForm,new UpdateWrapper<PurchaseOrderForm>().eq("id",purchaseOrderForm.getId()));
            return SaResult.ok("审核通过");
        }
        catch (Exception e){
            return SaResult.error(e.getMessage());
        }
    }
}



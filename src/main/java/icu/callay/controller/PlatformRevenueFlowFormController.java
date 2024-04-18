package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.service.PlatformRevenueFlowFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (PlatformRevenueFlowForm)表控制层
 *
 * @author Callay
 * @since 2024-04-10 14:33:56
 */
@RestController
@RequestMapping("platformRevenueFlowForm")
@RequiredArgsConstructor
public class PlatformRevenueFlowFormController {

    private final PlatformRevenueFlowFormService platformRevenueFlowFormService;

    @GetMapping("getPageByFrom")
    public SaResult getPageByForm(String type,String from,int page,int rows){
        return platformRevenueFlowFormService.getPageByForm(type,from,page,rows);
    }
}


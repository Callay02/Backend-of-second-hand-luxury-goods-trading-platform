package icu.callay.controller;

import cn.dev33.satoken.util.SaResult;
import com.tracking51.Tracking51;
import com.tracking51.exception.Tracking51Exception;
import com.tracking51.model.Tracking51Response;
import com.tracking51.model.tracking.CreateTrackingParams;
import com.tracking51.model.tracking.GetTrackingResultsParams;
import com.tracking51.model.tracking.Tracking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.controller
 * &#064;className:      TrackingController
 * &#064;author:     Callay
 * &#064;description:  对物流信息的一些操作
 * &#064;date:    2024/4/19 21:19
 * &#064;version:    1.0
 */
@RestController
@RequestMapping("51tracking")
public class TrackingController {

    @Value("${tracking.apiKey}")
    private String apiKey;


    /**
     * @param courierCode:
     * @param trackingNumber:
     * @return SaResult
     * @author Callay
     * &#064;description 创建物流订单
     * &#064;2024/4/19 21:46
     */
    @GetMapping("create")
    public SaResult create(String courierCode,String trackingNumber){
        try {
            Tracking51 tracking51 = new Tracking51(apiKey);
            CreateTrackingParams createTrackingParams = new CreateTrackingParams();
            createTrackingParams.setTrackingNumber(trackingNumber);
            createTrackingParams.setCourierCode(courierCode);
            Tracking51Response<Tracking> result = tracking51.trackings.CreateTracking(createTrackingParams);

            if(result.getData() != null){
                Tracking trackings = result.getData();
                return SaResult.data(trackings);
            }
            return SaResult.error();
        } catch (Tracking51Exception | IOException e) {
            return SaResult.error(e.getMessage());
        }
    }

    /**
     * @param courierCode:
     * @param trackingNumber:
     * @return SaResult
     * @author Callay
     * &#064;description 获取物流信息
     * &#064;2024/4/19 21:46
     */
    @GetMapping("get")
    public SaResult get(String courierCode,String trackingNumber){
        try {
            Tracking51 tracking51 = new Tracking51(apiKey);
            GetTrackingResultsParams trackingParams = new GetTrackingResultsParams();
            trackingParams.setTrackingNumbers(trackingNumber);
            trackingParams.setCourierCode(courierCode);
            //trackingParams.setCreatedDateMin("2023-08-23T06:00:00+00:00");
            //trackingParams.setCreatedDateMax("2024-09-18T06:00:00+00:00");
            Tracking51Response result = tracking51.trackings.GetTrackingResults(trackingParams);
            if(result.getMeta().getCode()==200){
                return SaResult.data(result.getData());
            }
            return SaResult.error();
        } catch (Tracking51Exception | IOException e) {
            return SaResult.error(e.getMessage());
        }
    }
}

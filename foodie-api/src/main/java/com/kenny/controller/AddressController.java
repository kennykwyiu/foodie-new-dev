package com.kenny.controller;

import com.kenny.bo.AddressBO;
import com.kenny.pojo.UserAddress;
import com.kenny.service.AddressService;
import com.kenny.utils.JsonResult;
import com.kenny.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "Address Related", tags = {"APIs related to addresses"})
@RequestMapping("address")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * On the order confirmation page, users can perform the following operations regarding shipping addresses:
     * 1. Retrieve a list of all user shipping addresses
     * 2. Add a new shipping address
     * 3. Delete a shipping address
     * 4. Update a shipping address
     * 5. Set a default shipping address
     */

    @ApiOperation(value = "Query Shipping Address List by User ID", notes = "Query Shipping Address List by User ID", httpMethod = "POST")
    @PostMapping("/list")
    public JsonResult list(
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);
        return JsonResult.ok(list);
    }

    @ApiOperation(value = "Add New Address for User", notes = "Add New Address for User", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(@RequestBody AddressBO addressBO) {

        JsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);

        return JsonResult.ok();
    }

    private JsonResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JsonResult.errorMsg("Recipient cannot be empty");
        }
        if (receiver.length() > 12) {
            return JsonResult.errorMsg("Recipient name cannot be too long");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JsonResult.errorMsg("Recipient mobile number cannot be empty");
        }
        if (mobile.length() != 11) {
            return JsonResult.errorMsg("Recipient mobile number length is incorrect");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JsonResult.errorMsg("Recipient mobile number format is incorrect");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JsonResult.errorMsg("Shipping address information cannot be empty");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "Update User Address", notes = "Update User Address", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JsonResult.errorMsg("Update address error: addressId cannot be empty");
        }

        JsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);

        return JsonResult.ok();
    }
}

package com.kenny.service;

import com.kenny.bo.AddressBO;
import com.kenny.pojo.UserAddress;

import java.util.List;

public interface AddressService {
    public List<UserAddress> queryAll(String userId);
    public void addNewUserAddress(AddressBO addressBO);
    public void updateUserAddress(AddressBO addressBO);
    public void deleteUserAddress(String userId, String addressId);
    public void updateUserAddressToBeDefault(String userId, String addressId);
}

package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.receipt.ReceiptRequestDto;
import com.clothes.websitequanao.dto.request.receipt.UpdaterReceiptRequest;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface ReceiptService {

    ServiceResponse getAll();


    ServiceResponse getReceiptById(Long id);

    //product
    ServiceResponse getAllProduct();

    // add receipt
    ServiceResponse addReceipt(ReceiptRequestDto dto);
    // update user manu

    ServiceResponse updateUserManu(UpdaterReceiptRequest dto);

    ServiceResponse updateStatusReceipt(Long receiptId);

    ServiceResponse cancelReceipt(Long receiptId);
}

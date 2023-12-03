package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.bill.BillUserRequestDTO;
import com.clothes.websitequanao.dto.request.cart.CartRequestDTO;
import com.clothes.websitequanao.dto.request.cart.CartRequestNoLoginDTO;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface BillService {
    // USSER
    ServiceResponse getAll();

    // localStorage
    ServiceResponse getAllNoLogin(CartRequestNoLoginDTO dto);

    ServiceResponse addCartLocalstorage(CartRequestNoLoginDTO dto);

    // localStorage
    ServiceResponse addCart(CartRequestDTO dto);

    ServiceResponse changQuantity(CartRequestDTO dto);

    ServiceResponse deleteProductOfCart(CartRequestDTO dto);

    ServiceResponse totalCart();

    ServiceResponse bill(BillUserRequestDTO dto);


    // user quản lý bill
    ServiceResponse getAllByStatus(int status, int page, int limit);

    ServiceResponse totalByStatus(int status);

    ServiceResponse getBillDetailByIdUser(Long id);

    // pay now
    ServiceResponse getBillPayNow(Long productId, Integer quantity);
    ServiceResponse addBillPayNow(BillUserRequestDTO dto);
    // USER

    //ADMIN
    ServiceResponse getAllAdmin();

    ServiceResponse getBillDetailById(Long id);


    ServiceResponse verifyBill(Long id);

    ServiceResponse cancelBill(Long id);

    ServiceResponse updateStatusBill(Long billId);

    // export pbf


    //
}

package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.subservices.SubServiceSaveRequest;
import com.example.final_project_part3_springmvc.dto.subservices.SubServicesSaveResponse;
import com.example.final_project_part3_springmvc.dto.wallet.WalletSaveRequest;
import com.example.final_project_part3_springmvc.dto.wallet.WalletSaveResponse;
import com.example.final_project_part3_springmvc.model.SubServices;
import com.example.final_project_part3_springmvc.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class );

    Wallet walletSaveRequestToModel(WalletSaveRequest request);
    WalletSaveResponse modelToWalletSaveResponse(Wallet wallet);
}

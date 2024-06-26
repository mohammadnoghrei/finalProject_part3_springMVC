package com.example.final_project_part3_springmvc.mapper;

import com.example.final_project_part3_springmvc.dto.offer.OfferSaveRequest;
import com.example.final_project_part3_springmvc.dto.offer.OfferSaveResponse;
import com.example.final_project_part3_springmvc.model.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class );

    Offer offerSaveRequestToModel(OfferSaveRequest request);
    OfferSaveResponse modelToOfferSaveResponse(Offer offer);
}

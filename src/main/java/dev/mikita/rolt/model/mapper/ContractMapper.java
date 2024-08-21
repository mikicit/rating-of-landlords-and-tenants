package dev.mikita.rolt.model.mapper;

import dev.mikita.rolt.dto.request.ContractCreateRequestDTO;
import dev.mikita.rolt.dto.request.ContractUpdateRequestDTO;
import dev.mikita.rolt.dto.response.ContractResponseDTO;
import dev.mikita.rolt.model.Contract;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toContract(ContractCreateRequestDTO contractResponseDTO);
    Contract toContract(ContractUpdateRequestDTO contractUpdateRequestDTO);
    ContractResponseDTO toContractResponseDTO(Contract contract);
}

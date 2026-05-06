package com.ipfnoslares.service;

import com.ipfnoslares.dto.EnderecoDTO;
import com.ipfnoslares.dto.EnderecoProximoDTO;
import com.ipfnoslares.dto.MembroDTO;
import com.ipfnoslares.dto.ViaCepResponseDTO;
import com.ipfnoslares.model.Endereco;
import com.ipfnoslares.model.Membro;
import com.ipfnoslares.repository.EnderecoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de negócio para operações de CRUD e busca por proximidade de endereços.
 */
@Service
public class EnderecoService {

    private static final Logger oLogger = LoggerFactory.getLogger(EnderecoService.class);

    private final EnderecoRepository oEnderecoRepository;
    private final ViaCepService oViaCepService;
    private final GeocodingService oGeocodingService;

    public EnderecoService(EnderecoRepository oEnderecoRepository,
                           ViaCepService oViaCepService,
                           GeocodingService oGeocodingService) {
        this.oEnderecoRepository = oEnderecoRepository;
        this.oViaCepService      = oViaCepService;
        this.oGeocodingService   = oGeocodingService;
    }

    // =========================================================
    // Listar todos
    // =========================================================

    @Transactional(readOnly = true)
    public List<EnderecoDTO> listarTodos() {
        return oEnderecoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // Buscar por ID
    // =========================================================

    @Transactional(readOnly = true)
    public EnderecoDTO buscarPorId(Long nId) {
        Endereco oEndereco = oEnderecoRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + nId));
        return toDTO(oEndereco);
    }

    // =========================================================
    // Criar
    // =========================================================

    @Transactional
    public EnderecoDTO criar(EnderecoDTO oDto) {
        // 1. Consulta o ViaCEP para obter logradouro, bairro, cidade, estado
        ViaCepResponseDTO oViaCep = oViaCepService.buscarPorCep(oDto.getSCep());
        if (oViaCep == null) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + oDto.getSCep());
        }

        // 2. Preenche os campos vindos do ViaCEP
        String sLogradouro = (oViaCep.getSLogradouro() != null && !oViaCep.getSLogradouro().isBlank())
                ? oViaCep.getSLogradouro()
                : oDto.getSLogradouro();

        // 3. Geocodifica para obter latitude e longitude
        Double[] aCoordenadas = oGeocodingService.geocodificar(
                sLogradouro, oDto.getSNumero(),
                oViaCep.getSBairro(), oViaCep.getSCidade(), oViaCep.getSEstado());

        Double dLatitude  = (aCoordenadas != null) ? aCoordenadas[0] : null;
        Double dLongitude = (aCoordenadas != null) ? aCoordenadas[1] : null;

        if (aCoordenadas == null) {
            oLogger.warn("Não foi possível geocodificar o endereço do CEP {}. "
                    + "Endereço será salvo sem coordenadas.", oDto.getSCep());
        }

        // 4. Constrói a entidade via Builder
        Endereco oEndereco = Endereco.builder()
                .sCep(oViaCep.getSCep())
                .sLogradouro(sLogradouro)
                .sNumero(oDto.getSNumero())
                .sComplemento(oDto.getSComplemento())
                .sBairro(oViaCep.getSBairro())
                .sCidade(oViaCep.getSCidade())
                .sEstado(oViaCep.getSEstado())
                .dLatitude(dLatitude)
                .dLongitude(dLongitude)
                .build();

        Endereco oSalvo = oEnderecoRepository.save(oEndereco);
        oLogger.info("Endereço criado com id: {}", oSalvo.getNId());
        return toDTO(oSalvo);
    }

    // =========================================================
    // Atualizar
    // =========================================================

    @Transactional
    public EnderecoDTO atualizar(Long nId, EnderecoDTO oDto) {
        // Verifica existência
        oEnderecoRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + nId));

        // Consulta ViaCEP com o novo CEP (pode ter mudado)
        ViaCepResponseDTO oViaCep = oViaCepService.buscarPorCep(oDto.getSCep());
        if (oViaCep == null) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + oDto.getSCep());
        }

        String sLogradouro = (oViaCep.getSLogradouro() != null && !oViaCep.getSLogradouro().isBlank())
                ? oViaCep.getSLogradouro()
                : oDto.getSLogradouro();

        // Regeocodifica com os novos dados
        Double[] aCoordenadas = oGeocodingService.geocodificar(
                sLogradouro, oDto.getSNumero(),
                oViaCep.getSBairro(), oViaCep.getSCidade(), oViaCep.getSEstado());

        Double dLatitude  = (aCoordenadas != null) ? aCoordenadas[0] : null;
        Double dLongitude = (aCoordenadas != null) ? aCoordenadas[1] : null;

        // Constrói nova instância com o mesmo ID (JPA fará UPDATE)
        Endereco oAtualizado = Endereco.builder()
                .nId(nId)
                .sCep(oViaCep.getSCep())
                .sLogradouro(sLogradouro)
                .sNumero(oDto.getSNumero())
                .sComplemento(oDto.getSComplemento())
                .sBairro(oViaCep.getSBairro())
                .sCidade(oViaCep.getSCidade())
                .sEstado(oViaCep.getSEstado())
                .dLatitude(dLatitude)
                .dLongitude(dLongitude)
                .build();

        Endereco oSalvo = oEnderecoRepository.save(oAtualizado);
        oLogger.info("Endereço atualizado: id={}", oSalvo.getNId());
        return toDTO(oSalvo);
    }

    // =========================================================
    // Excluir
    // =========================================================

    @Transactional
    public void excluir(Long nId) {
        if (!oEnderecoRepository.existsById(nId)) {
            throw new RuntimeException("Endereço não encontrado com id: " + nId);
        }
        oEnderecoRepository.deleteById(nId);
        oLogger.info("Endereço excluído: id={}", nId);
    }

    // =========================================================
    // Consultar ViaCEP (sem salvar — usado para autopreenchimento)
    // =========================================================

    public EnderecoDTO consultarViaCep(String sCep) {
        ViaCepResponseDTO oViaCep = oViaCepService.buscarPorCep(sCep);
        if (oViaCep == null) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + sCep);
        }

        return new EnderecoDTO(
                null,
                oViaCep.getSCep(),
                oViaCep.getSLogradouro(),
                null,
                oViaCep.getSComplemento(),
                oViaCep.getSBairro(),
                oViaCep.getSCidade(),
                oViaCep.getSEstado(),
                null, null, null, null, null
        );
    }

    // =========================================================
    // Buscar endereços próximos
    // =========================================================

    @Transactional(readOnly = true)
    public List<EnderecoProximoDTO> buscarProximos(String sCepReferencia, Double dRaioKm) {
        Double[] aCoordsRef = oGeocodingService.geocodificarPorCep(sCepReferencia);

        if (aCoordsRef == null) {
            throw new RuntimeException(
                    "Não foi possível determinar a localização para o CEP: " + sCepReferencia);
        }

        double dLatRef = aCoordsRef[0];
        double dLonRef = aCoordsRef[1];

        List<Endereco> lEnderecos = oEnderecoRepository.findAllComCoordenadas();

        return lEnderecos.stream()
                .map(oEndereco -> {
                    double dDistancia = oGeocodingService.calcularDistanciaKm(
                            dLatRef, dLonRef,
                            oEndereco.getDLatitude(), oEndereco.getDLongitude());

                    double dDistanciaArredondada = Math.round(dDistancia * 100.0) / 100.0;

                    // Converte os membros vinculados ao endereço para que o card
                    // exibido na listagem mostre nome, função e telefone.
                    List<MembroDTO> lMembrosDto = oEndereco.getLMembros()
                            .stream()
                            .map(this::membroToDTO)
                            .collect(Collectors.toList());

                    return new EnderecoProximoDTO(
                            oEndereco.getNId(),
                            oEndereco.getSCep(),
                            oEndereco.getSLogradouro(),
                            oEndereco.getSNumero(),
                            oEndereco.getSComplemento(),
                            oEndereco.getSBairro(),
                            oEndereco.getSCidade(),
                            oEndereco.getSEstado(),
                            oEndereco.getDLatitude(),
                            oEndereco.getDLongitude(),
                            dDistanciaArredondada,
                            lMembrosDto
                    );
                })
                .filter(oProximo -> dRaioKm == null || oProximo.getDDistanciaKm() <= dRaioKm)
                .sorted(Comparator.comparingDouble(EnderecoProximoDTO::getDDistanciaKm))
                .collect(Collectors.toList());
    }

    // =========================================================
    // Conversão Entidade → DTO (inclui membros vinculados)
    // =========================================================

    private EnderecoDTO toDTO(Endereco oEndereco) {
        // Converte a lista de membros vinculados para MembroDTO
        List<MembroDTO> lMembrosDto = oEndereco.getLMembros()
                .stream()
                .map(this::membroToDTO)
                .collect(Collectors.toList());

        return new EnderecoDTO(
                oEndereco.getNId(),
                oEndereco.getSCep(),
                oEndereco.getSLogradouro(),
                oEndereco.getSNumero(),
                oEndereco.getSComplemento(),
                oEndereco.getSBairro(),
                oEndereco.getSCidade(),
                oEndereco.getSEstado(),
                oEndereco.getDLatitude(),
                oEndereco.getDLongitude(),
                lMembrosDto,
                oEndereco.getDtCriacao(),
                oEndereco.getDtAtualizacao()
        );
    }

    /** Converte um Membro em MembroDTO (sem referência circular ao endereço) */
    private MembroDTO membroToDTO(Membro oMembro) {
        return new MembroDTO(
                oMembro.getNId(),
                oMembro.getSNome(),
                oMembro.getSTelefone(),
                oMembro.getSFuncao(),
                oMembro.getOEndereco() != null ? oMembro.getOEndereco().getNId() : null,
                oMembro.getDtCriacao(),
                oMembro.getDtAtualizacao()
        );
    }
}

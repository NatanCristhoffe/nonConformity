package blessed.sector.controller;

import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.service.SectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sectors")
public class SectorController {

    private final SectorService service;
    public SectorController(SectorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SectorResponseDTO>> getAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());

    }

    @PostMapping("/create")
    public ResponseEntity<SectorResponseDTO> createSector(
            @Valid @RequestBody SectorRequestDTO data
    ){
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SectorResponseDTO(service.create(data)));
    }

    @PutMapping("/{idSector}")
    public ResponseEntity<SectorResponseDTO> update(
            @PathVariable Long idSector,
            @RequestBody @Valid SectorRequestDTO data
            ){

        return ResponseEntity.ok(service.update(idSector, data));

    }

    @DeleteMapping("/delete/{idSector}")
    public ResponseEntity<Map<String, String>> deleteSectors(
            @PathVariable Long idSector
    ){
        service.disable(idSector);
        return ResponseEntity.ok(Map.of("success","setor deletado com sucesso."));
    }


    @PutMapping("/enable/{idSector}")
    public ResponseEntity<Map<String, String>> enableSectors(
            @PathVariable Long idSector
    ){
        service.enable(idSector);
        return ResponseEntity.ok(Map.of("success","setor ativo com sucesso."));
    }

    @GetMapping("/get-by")
    public ResponseEntity<List<SectorResponseDTO>> getByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "false") boolean getNotActive
    ){
        return ResponseEntity.ok(service.getByName(name, getNotActive));
    }
}

package com.manualbalanceddb.backend.contoller;

import com.manualbalanceddb.backend.DTO.PartitionResponse;
import com.manualbalanceddb.backend.model.Partition;
import com.manualbalanceddb.backend.repository.FileRepository;
import com.manualbalanceddb.backend.repository.PartitionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/partitions")
@CrossOrigin
public class PartitionController {

    private final PartitionRepository partitionRepository;
    private final FileRepository fileRepository;

    public PartitionController(
            PartitionRepository partitionRepository,
            FileRepository fileRepository) {

        this.partitionRepository = partitionRepository;
        this.fileRepository = fileRepository;
    }

    @PostMapping
    public Partition create(
            @RequestBody Partition partition) {

        return partitionRepository.save(partition);
    }

    @GetMapping("/user/{userId}")
    public List<PartitionResponse> getByUser(
            @PathVariable String userId) {

        List<Partition> partitions =
                partitionRepository.findByUserId(userId);

        return partitions.stream()
                .map(partition -> {

                    Long used =
                            fileRepository.getPartitionStorage(
                                    userId,
                                    partition.getName());
                    if (used == null) {
                        used = 0L;
                    }

                    long count =
                            fileRepository.countByUserIdAndPartition(
                                    userId,
                                    partition.getName());

                    if(count == null) {
                        count = 0l;
                    }

                    long totalSpace = 5L * 1024 * 1024 * 1024;

                    double percentage = (used * 100.0) / totalSpace;

                    PartitionResponse dto =
                            new PartitionResponse();

                    dto.setId(partition.getId());
                    dto.setName(partition.getName());
                    dto.setIcon(partition.getIcon());
                    dto.setStatus(partition.getStatus());

                    dto.setUsedSpace(used);
                    dto.setFileCount(count);
                    dto.setPercentage(Math.min(percentage, 100));

                    return dto;
                })
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String userId) {

        Partition partition =
                partitionRepository.findById(id)
                .orElseThrow();

        if (!partition.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "Access denied");
        }

        partitionRepository.delete(partition);
    }
}
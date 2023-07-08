package niffler.service;

import com.google.protobuf.Timestamp;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import niffler.data.CategoryEntity;
import niffler.data.SpendEntity;
import niffler.data.repository.CategoryRepository;
import niffler.data.repository.SpendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@GrpcService
public class GrpcSpendService extends NifflerSpendServiceGrpc.NifflerSpendServiceImplBase {
    private final SpendRepository spendRepository;
    private final GrpcCurrencyClient grpcCurrencyClient;
    private final CategoryRepository categoryRepository;
    private static final int MAX_CATEGORIES_SIZE = 7;


    @Autowired
    public GrpcSpendService(SpendRepository spendRepository, GrpcCurrencyClient grpcCurrencyClient, CategoryRepository categoryRepository) {
        this.spendRepository = spendRepository;
        this.grpcCurrencyClient = grpcCurrencyClient;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void getSpendsForUser(SpendsRequest request, StreamObserver<SpendsResponse> responseObserver) {

        List<SpendEntity> spendEntities = spendRepository.findAllByUsername(request.getUsername());
        SpendsResponse spendsResponse = SpendsResponse.newBuilder().addAllAllSpendsForUser(spendEntities.stream()
                        .map(e -> (Spend.newBuilder().setId(e.getId().toString())
                                .setSpendDate(Timestamp.newBuilder().setSeconds(e.getSpendDate().toInstant().getEpochSecond()).setNanos(e.getSpendDate().toInstant().getNano()).build())
                                .setCategory(e.getCategory().getCategory())
                                .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(e.getCurrency().name()))
                                .setAmount(e.getAmount())
                                .setDescription(e.getDescription())
                                .setUsername(e.getUsername())
                                .build()))
                        .toList())
                .build();
        responseObserver.onNext(spendsResponse);
        responseObserver.onCompleted();

    }

    @Override
    public void addSpendForUser(AddSpendRequest request, StreamObserver<AddSpendResponse> responseObserver) {
        Timestamp timestamp = request.getSpend().getSpendDate();
        Date spendDateFromRequest = new Date(timestamp.getSeconds() * 1000L);
        final String category = request.getSpend().getCategory();
        CategoryEntity categoryEntity = categoryRepository.findAllByUsername(request.getSpend().getUsername())
                .stream()
                .filter(c -> c.getCategory().equals(category))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(request.getSpend().getUsername());
        spendEntity.setSpendDate(spendDateFromRequest);
        spendEntity.setCategory(categoryEntity);
        spendEntity.setCurrency(niffler.model.CurrencyValues.valueOf(request.getSpend().getCurrency().name()));
        spendEntity.setAmount(request.getSpend().getAmount());
        spendEntity.setDescription(request.getSpend().getDescription());
        spendRepository.save(spendEntity);

        AddSpendResponse addSpendResponse = AddSpendResponse.newBuilder()
                .setUsername(request.getSpend().getUsername())
                .setSpend(Spend.newBuilder().setId(spendEntity.getId().toString())
                        .setSpendDate(Timestamp.newBuilder().setSeconds(spendEntity.getSpendDate().toInstant().getEpochSecond()).setNanos(spendEntity.getSpendDate().toInstant().getNano()).build())
                        .setCategory(spendEntity.getCategory().getCategory())
                        .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(spendEntity.getCurrency().name()))
                        .setAmount(spendEntity.getAmount())
                        .setDescription(spendEntity.getDescription())
                        .setUsername(spendEntity.getUsername()))
                .build();


        responseObserver.onNext(addSpendResponse);
        responseObserver.onCompleted();

    }


    @Override
    public void getCategories(CategoriesRequest request, StreamObserver<CategoriesResponse> responseObserver) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByUsername(request.getUsername());
        CategoriesResponse categoriesResponse = CategoriesResponse.newBuilder().addAllCategory(categoryEntities.stream()
                        .map(e -> (Category.newBuilder().setUsername(e.getUsername())
                                .setId(e.getId().toString())
                                .setCategory(e.getCategory())
                                .build()))
                        .toList())
                .build();
        responseObserver.onNext(categoriesResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void addCategoriesForUser(AddCategoryRequest request, StreamObserver<AddCategoryResponse> responseObserver) {


        if (categoryRepository.findAllByUsername(request.getUsername()).size() > MAX_CATEGORIES_SIZE) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Can`t add over than 7 categories for user: '" + request.getUsername());
        }
        CategoryEntity ce = new CategoryEntity();
        ce.setCategory(request.getCategory().getCategory());
        ce.setUsername(request.getUsername());
        categoryRepository.save(ce);
        AddCategoryResponse categoryResponse = AddCategoryResponse.newBuilder()
                .setUsername(ce.getUsername())
                .setCategory(Category.newBuilder()
                        .setCategory(ce.getCategory())
                        .setId(ce.getId().toString()).build())
                .setUsername(ce.getUsername())
                .build();
        responseObserver.onNext(categoryResponse);
        responseObserver.onCompleted();
    }
}



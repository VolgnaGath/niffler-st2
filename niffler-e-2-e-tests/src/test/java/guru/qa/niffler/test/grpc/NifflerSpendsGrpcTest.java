package guru.qa.niffler.test.grpc;

import com.google.protobuf.Timestamp;
import guru.qa.grpc.niffler.grpc.*;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NifflerSpendsGrpcTest extends BaseGrpcTest{
    @Test
    void getSpendForUser() {
        String user = "dima";
        SpendsRequest spendsRequest = SpendsRequest.newBuilder().setUsername(user).build();
        final SpendsResponse spendsResponse = spendStub.getSpendsForUser(spendsRequest);
        System.out.println(spendsResponse);
        Assertions.assertNotNull(spendsResponse);
    }
    @Test
    void addSpendForUserTest() {
        Date date = new Date(688515200000L);
        AddSpendRequest request = AddSpendRequest.newBuilder()
                .setSpend(Spend.newBuilder()
                        .setSpendDate(Timestamp.newBuilder().setSeconds(date.toInstant().getEpochSecond()).setNanos(date.toInstant().getNano()).build())
                        .setCategory("test1")
                        .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.RUB)
                        .setAmount(52000)
                        .setDescription("GrpcTest")
                        .setUsername("dima").build()).build();
        AddSpendResponse response = spendStub.addSpendForUser(request);
        Assertions.assertNotNull(response.getSpend().getId());
        Assertions.assertEquals(request.getSpend().getUsername(), response.getUsername());
        Assertions.assertEquals(request.getSpend().getAmount(), response.getSpend().getAmount());
        Assertions.assertEquals(request.getSpend().getDescription(), response.getSpend().getDescription());
        Assertions.assertEquals(request.getSpend().getCurrency(), response.getSpend().getCurrency());
    }
    @Test
    void getCategories() {
        CategoriesRequest categoriesRequest = CategoriesRequest.newBuilder().setUsername("dima").build();
        CategoriesResponse categoriesResponse = spendStub.getCategories(categoriesRequest);
        List<Category> categoryList = categoriesResponse.getCategoryList();
        assertEquals("testCategory", categoryList.get(0).getCategory());
        assertEquals("test1", categoryList.get(1).getCategory());
        assertEquals("new", categoryList.get(2).getCategory());
    }

    @Test
    void addCategoriesForUser() {
        String category = DataUtils.generateNewCategory();
        AddCategoryRequest addCategoryRequest = AddCategoryRequest.newBuilder()
                .setUsername("dima")
                .setCategory(Category.newBuilder()
                        .setUsername("dima")
                        .setCategory(category)
                        .build())
                .build();
        AddCategoryResponse addCategoryResponse = spendStub.addCategoriesForUser(addCategoryRequest);
        assertEquals(category, addCategoryResponse.getCategory().getCategory());
    }
}

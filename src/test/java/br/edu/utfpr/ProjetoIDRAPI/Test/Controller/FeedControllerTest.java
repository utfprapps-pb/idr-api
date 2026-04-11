package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.enums.FeedType;

public class FeedControllerTest extends CrudControllerTest<Feed, Feed, Long> {

    @Override
    protected Feed createValidObject() {
        return Feed.builder()
                .name("Test")
                .type(FeedType.CONCENTRADO)
                .build();
    }

    @Override
    protected Feed createInvalidObject() {
        return Feed.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/feeds";
    }
}

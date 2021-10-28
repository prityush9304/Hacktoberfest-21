package com.sample.moviecatalogservice.resources;

import com.sample.moviecatalogservice.models.CatalogItem;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/catalog")
@ApplicationScoped
public class MovieCatalogResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CatalogItem> getMovies() {
        List<CatalogItem> a=new ArrayList<>();
        a.add(new CatalogItem("tital","hey",12));
        return a;
    }
}

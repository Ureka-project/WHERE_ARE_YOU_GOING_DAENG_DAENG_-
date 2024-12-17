package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlace is a Querydsl query type for Place
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlace extends EntityPathBase<Place> {

    private static final long serialVersionUID = -1849264565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlace place = new QPlace("place");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    public final StringPath city = createString("city");

    public final StringPath cityDetail = createString("cityDetail");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final ListPath<com.daengdaeng_eodiga.project.favorite.entity.Favorite, com.daengdaeng_eodiga.project.favorite.entity.QFavorite> favorite = this.<com.daengdaeng_eodiga.project.favorite.entity.Favorite, com.daengdaeng_eodiga.project.favorite.entity.QFavorite>createList("favorite", com.daengdaeng_eodiga.project.favorite.entity.Favorite.class, com.daengdaeng_eodiga.project.favorite.entity.QFavorite.class, PathInits.DIRECT2);

    public final BooleanPath indoor = createBoolean("indoor");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final ListPath<OpeningDate, QOpeningDate> openingDates = this.<OpeningDate, QOpeningDate>createList("openingDates", OpeningDate.class, QOpeningDate.class, PathInits.DIRECT2);

    public final BooleanPath outdoor = createBoolean("outdoor");

    public final BooleanPath parking = createBoolean("parking");

    public final NumberPath<Integer> placeId = createNumber("placeId", Integer.class);

    public final QPlaceScore placeScores;

    public final StringPath placeType = createString("placeType");

    public final StringPath postCode = createString("postCode");

    public final ListPath<com.daengdaeng_eodiga.project.review.entity.Review, com.daengdaeng_eodiga.project.review.entity.QReview> reviews = this.<com.daengdaeng_eodiga.project.review.entity.Review, com.daengdaeng_eodiga.project.review.entity.QReview>createList("reviews", com.daengdaeng_eodiga.project.review.entity.Review.class, com.daengdaeng_eodiga.project.review.entity.QReview.class, PathInits.DIRECT2);

    public final StringPath streetAddresses = createString("streetAddresses");

    public final StringPath telNumber = createString("telNumber");

    public final StringPath thumbImgPath = createString("thumbImgPath");

    public final StringPath township = createString("township");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public final ListPath<com.daengdaeng_eodiga.project.visit.entity.Visit, com.daengdaeng_eodiga.project.visit.entity.QVisit> visits = this.<com.daengdaeng_eodiga.project.visit.entity.Visit, com.daengdaeng_eodiga.project.visit.entity.QVisit>createList("visits", com.daengdaeng_eodiga.project.visit.entity.Visit.class, com.daengdaeng_eodiga.project.visit.entity.QVisit.class, PathInits.DIRECT2);

    public final StringPath weightLimit = createString("weightLimit");

    public QPlace(String variable) {
        this(Place.class, forVariable(variable), INITS);
    }

    public QPlace(Path<? extends Place> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlace(PathMetadata metadata, PathInits inits) {
        this(Place.class, metadata, inits);
    }

    public QPlace(Class<? extends Place> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.placeScores = inits.isInitialized("placeScores") ? new QPlaceScore(forProperty("placeScores"), inits.get("placeScores")) : null;
    }

}


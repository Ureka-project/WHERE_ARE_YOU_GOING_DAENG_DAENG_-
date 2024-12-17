package com.daengdaeng_eodiga.project.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -2133836517L;

    public static final QUser user = new QUser("user");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    public final StringPath city = createString("city");

    public final StringPath cityDetail = createString("cityDetail");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final ListPath<com.daengdaeng_eodiga.project.favorite.entity.Favorite, com.daengdaeng_eodiga.project.favorite.entity.QFavorite> favorites = this.<com.daengdaeng_eodiga.project.favorite.entity.Favorite, com.daengdaeng_eodiga.project.favorite.entity.QFavorite>createList("favorites", com.daengdaeng_eodiga.project.favorite.entity.Favorite.class, com.daengdaeng_eodiga.project.favorite.entity.QFavorite.class, PathInits.DIRECT2);

    public final StringPath gender = createString("gender");

    public final StringPath nickname = createString("nickname");

    public final EnumPath<com.daengdaeng_eodiga.project.oauth.OauthProvider> oauthProvider = createEnum("oauthProvider", com.daengdaeng_eodiga.project.oauth.OauthProvider.class);

    public final ListPath<com.daengdaeng_eodiga.project.pet.entity.Pet, com.daengdaeng_eodiga.project.pet.entity.QPet> pets = this.<com.daengdaeng_eodiga.project.pet.entity.Pet, com.daengdaeng_eodiga.project.pet.entity.QPet>createList("pets", com.daengdaeng_eodiga.project.pet.entity.Pet.class, com.daengdaeng_eodiga.project.pet.entity.QPet.class, PathInits.DIRECT2);

    public final ListPath<com.daengdaeng_eodiga.project.preference.entity.Preference, com.daengdaeng_eodiga.project.preference.entity.QPreference> preferences = this.<com.daengdaeng_eodiga.project.preference.entity.Preference, com.daengdaeng_eodiga.project.preference.entity.QPreference>createList("preferences", com.daengdaeng_eodiga.project.preference.entity.Preference.class, com.daengdaeng_eodiga.project.preference.entity.QPreference.class, PathInits.DIRECT2);

    public final ListPath<com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog, com.daengdaeng_eodiga.project.region.entity.QRegionOwnerLog> regionOwnerLogs = this.<com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog, com.daengdaeng_eodiga.project.region.entity.QRegionOwnerLog>createList("regionOwnerLogs", com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog.class, com.daengdaeng_eodiga.project.region.entity.QRegionOwnerLog.class, PathInits.DIRECT2);

    public final ListPath<com.daengdaeng_eodiga.project.region.entity.RegionVisitDay, com.daengdaeng_eodiga.project.region.entity.QRegionVisitDay> regionVisitDays = this.<com.daengdaeng_eodiga.project.region.entity.RegionVisitDay, com.daengdaeng_eodiga.project.region.entity.QRegionVisitDay>createList("regionVisitDays", com.daengdaeng_eodiga.project.region.entity.RegionVisitDay.class, com.daengdaeng_eodiga.project.region.entity.QRegionVisitDay.class, PathInits.DIRECT2);

    public final ListPath<com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal, com.daengdaeng_eodiga.project.region.entity.QRegionVisitTotal> regionVisitTotals = this.<com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal, com.daengdaeng_eodiga.project.region.entity.QRegionVisitTotal>createList("regionVisitTotals", com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal.class, com.daengdaeng_eodiga.project.region.entity.QRegionVisitTotal.class, PathInits.DIRECT2);

    public final ListPath<com.daengdaeng_eodiga.project.review.entity.Review, com.daengdaeng_eodiga.project.review.entity.QReview> reviews = this.<com.daengdaeng_eodiga.project.review.entity.Review, com.daengdaeng_eodiga.project.review.entity.QReview>createList("reviews", com.daengdaeng_eodiga.project.review.entity.Review.class, com.daengdaeng_eodiga.project.review.entity.QReview.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final ListPath<com.daengdaeng_eodiga.project.visit.entity.Visit, com.daengdaeng_eodiga.project.visit.entity.QVisit> visits = this.<com.daengdaeng_eodiga.project.visit.entity.Visit, com.daengdaeng_eodiga.project.visit.entity.QVisit>createList("visits", com.daengdaeng_eodiga.project.visit.entity.Visit.class, com.daengdaeng_eodiga.project.visit.entity.QVisit.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}


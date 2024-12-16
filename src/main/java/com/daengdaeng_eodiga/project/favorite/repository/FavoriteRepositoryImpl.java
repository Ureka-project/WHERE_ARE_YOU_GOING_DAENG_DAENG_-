package com.daengdaeng_eodiga.project.favorite.repository;

import com.daengdaeng_eodiga.project.favorite.entity.QFavorite;
import com.daengdaeng_eodiga.project.place.entity.QOpeningDate;
import com.daengdaeng_eodiga.project.place.entity.QPlace;
import com.daengdaeng_eodiga.project.place.entity.QPlaceMedia;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Tuple> findCustomFavorites(int userId, LocalDateTime lastUpdatedAt, int lastFavoriteId, int size) {
        QFavorite f = QFavorite.favorite;
        QPlace p = QPlace.place;
        QOpeningDate od = QOpeningDate.openingDate;

        return queryFactory
                .select(f.favoriteId, p.placeId, p.name, p.thumbImgPath, p.placeType, p.streetAddresses,
                        p.latitude, p.longitude, od.startTime, od.endTime, f.updatedAt)
                .from(f)
                .join(f.place, p)
                .leftJoin(p.openingDates, od)
                .where(
                        f.user.userId.eq(userId),
                        nonOffsetCondition(f.updatedAt, f.favoriteId, lastUpdatedAt, lastFavoriteId)
                )
                .orderBy(f.updatedAt.desc(),
                        Expressions.numberPath(Long.class, f.favoriteId.getMetadata()).desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression nonOffsetCondition(DateTimePath<LocalDateTime> updatedAt,
                                                 NumberPath<Integer> favoriteId,
                                                 LocalDateTime lastUpdatedAt, int  lastFavoriteId) {
        if (lastUpdatedAt == null || lastFavoriteId == 0) {
            return null;
        }
        return updatedAt.lt(lastUpdatedAt)
                .or(updatedAt.eq(lastUpdatedAt).and(favoriteId.lt(lastFavoriteId)));
    }
}

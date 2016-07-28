package uk.co.mezpahlan.thedroidpicture.rss.item;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Created by mpahlan on 27/07/16.
 */
public interface ItemMvp {

    interface View extends BaseMvp.View <List<RssItem.Photo>> {

    }

    interface Presenter {

    }

    interface ModelInteractor extends BaseMvp.ModelInteractor {

    }
}

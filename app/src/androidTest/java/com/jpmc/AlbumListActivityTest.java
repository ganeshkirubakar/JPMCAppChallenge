package com.jpmc;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jpmc.model.Album;
import com.jpmc.view.AlbumListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class AlbumListActivityTest {
    @Rule
    public ActivityTestRule<AlbumListActivity> activityTestRule =
            new ActivityTestRule<>(AlbumListActivity.class);
    private boolean isNetworkAvailable;

    @Test
    public void verifyNetworkAvailability() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isNetworkAvailable = activityTestRule.getActivity().checkNetworkConnection();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if(isNetworkAvailable) {
            onView(withId(R.id.album_progress)).check(matches(not(ViewMatchers.isDisplayed())));
            onView(withId(R.id.items_unavailable)).check(matches(not(isDisplayed())));
        }
    }
    @Test
    public void testAlbumRecyclerViewItem_shouldHaveAlbumList(){
        Album album = new Album();
        album.setId(1);
        album.setUserID(1);
        album.setTitle("ab voluptas nostrum et nihil");
        onView(withId(R.id.album_list)).check(matches(hasAlbumListForPosition(1, album)));
    }

    @Test
    public void testAlbumRecyclerViewItem_shouldNotHaveAlbumList(){
        Album album = new Album();
        album.setId(1);
        album.setUserID(1);
        album.setTitle("ab voluptas nostrum et nihil");
        onView(withId(R.id.album_list)).check(matches(hasAlbumListForPosition(5, album)));
    }

    private static Matcher<View> hasAlbumListForPosition(final int position, final Album album){
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Item has Album data at position:"+ position +":");
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                if(item == null) {
                    return false;
                }
                RecyclerView.ViewHolder viewHolder = item.findViewHolderForAdapterPosition(position);
                if(viewHolder == null){
                    return false;
                }
                return !withChild(withText(album.getTitle())).matches(viewHolder.itemView);
            }
        };
    }
}

package org.holovin.intellij.DDDSortBookmarks;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.text.Collator;
import java.util.List;

public class SortBookmarksAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        BookmarkManager bookmarkManager  = BookmarkManager.getInstance(project);
        List<Bookmark> validBookmarks    = bookmarkManager.getValidBookmarks();

        int size = validBookmarks.size();

        if (size == 0) {
            return;
        }

        // TODO: pass locale en-en?
        Collator collator = Collator.getInstance();

        // Reverse order because add bookmarks to list from end
        validBookmarks.sort((a, b) -> collator.compare( b.getDescription(), a.getDescription()) );

        // remove old
        for (Bookmark bookmark : validBookmarks) {
            bookmarkManager.removeBookmark(bookmark);
        }

        // add new with right order
        for (Bookmark bookmark : validBookmarks) {
            bookmarkManager.addTextBookmark(
                    bookmark.getFile(),
                    bookmark.getLine(),
                    bookmark.getDescription()
            );
        }

        // Messages.showInfoMessage("ok", "Sorted");
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();

        e.getPresentation().setEnabled( project != null );
    }
}



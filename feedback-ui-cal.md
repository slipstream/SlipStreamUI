
Feedback for UI Design (C. Loomis)
==================================

General Comments
----------------

The new UI layout and design is overall excellent.  The design is much
cleaner and more polished than the existing one.  The uniformity in
the layout should make using the application more intuitive and
straightforward.

### Responsive Design

The current layout/stylesheet is not responsive at all.  It generally
works fine in a computer's browser, but doesn't scale/flow reasonably
when viewed on tablet/smartphone screen sizes.  This is something that
we should try to do as the application is likely to be used (at least
minimally) from such devices.  Firefox has a nice "Responsive Design
View" feature to see what the layout looks like on the various
devices.

### Banner

I think that the banner takes up too much screen real estate for the
generally small amount of information it conveys.  Perhaps about
two-thirds of its current size would be a reasonable compromise.  In
reducing the size, perhaps the third level of text can be removed.
This level of detail is probably appropriate as an additional drop
down panel or as a tooltip.

Regarding the icons in the banners, this is not completely consistent
between the various pages.  It would be more effective if an image
were used for all pages.  (Ideas for this in the comments for
individual pages.)

### Breadcrumbs

The breadcrumbs above the accordian are really not useful.  They
simply duplicate information that is in the banner (and title) on most
pages.  The only link that is important that isn't elsewhere is the
"home icon".  This could easily be moved up to the header line along
with the other major category links.

So the changes I'd suggest for this is:
  * Remove breadcrumb line above accordian.
  * Add "home icon" to the top red menu line.
  * Move top-level buttons from right to left side.
  * Make resource name elements in banner active links.
  * Use only generic name or last element in resource name in title.

### Footer

I don't think that we really benefit much from having the "swiss make
software" link in the footer.  This is probably something that would
be better as a panel of the login page, if it isn't removed entirely. 

There is really no reason to use a bit.ly link to the "swiss made
software" site.  If this is kept, the full link should be used.

I think that the footer would look better having the SixSq logo
separate the copyright from the version number.  Making it slightly
larger and putting it in red would emphasize it.  It should also be an
active link to the SixSq website.

### Accordian

The accordian layout is generally good, although I think it would be
further improved with:
  * Having more legible open/close boxes for the sections.  I find the
  current ones almost impossible to see the state.  It also isn't
  obvious that they are clickable.  May highlight them in green like
  internal buttons? 
  * Add an open all/close all icon to the accordian.  This allows
  people to see all of the information by scrolling without having to
  individually open up each accordian section.
  * Internal tabs are not visually distinct from the accordian
  headers.  It would help if they were a different color.  Maybe a
  slightly lighter shade of gray?
  * Buttons at the end of an accordian section are often clipped by
  the next section header.  More vertical space should be added to
  ensure this doesn't happen.

### Top Red Menu Line

This would be improved by using icons for the links rather than text.
A good model is the top line of the GitHub pages which use tooltips to
explain each icon.  (Add the 'home' icon here as discussed before.)

### Icons

I really don't find the brushed aluminum icons with letters very
appealing.  Finding appropriate graphical icons would be much more
effective.  


Login Page
----------

  * Ensure that the 'trade' entity is used consistently everywhere.
    (This has been pushed to repository.)
  * Change 'email' to 'Email' in registration section.
  * Find an appropriate icon for each panel of the banner.  Perhaps
    the SixSq logo for the welcome panel? 


Welcome Page
------------

Although I generally favor having fewer pages, I think that this page
is trying to do too many different things.  Specifically, the
consumption and metering functions would be better off on a separate
page.

I'd like to see the various "Modules" sections reduced to a single
section with perhaps additional column(s) to distinquish the various
types rather than separate sections.  It may be worthwhile to make
this an "active" table where the columns can be sorted or filtered.
The modules section should be the first on the page. 

Remove duplication in table between "Category" and the category icon
(as you've mentioned in the README).

The "Deployments, Runs, etc." section would be the only other section
on this page.  In this section:
  * Could this section be renamed to something shorter like "Current
  Activity"? 
  * What is the difference between a deployment and a simple run?  Is
  this distinction important? 
  * Can the tabs be replaced with a column giving the type of
  activity?  Making this an active table would then allow people to
  easily find what they are looking for.
  * Replace full UUID with just first 5 characters with the full UUID
  available as a tooltip. 

In the banner, an appropriate icon would be the "home" icon.  To make
the banner useful, a summary of the current activity could be added
(e.g. number of deployments, VMs, etc.).

Is the "New Project" button as a top-level button appropriate?  It
really is only linked to the "Modules" section.


Consumption and Metering
------------------------

As stated above, I think this would be better on a dedicated page with
a link provided from the Top Red Menu Line. 

I think that we'll probably need a face-to-face discussion on how to
make the presentation of the consumption and metering effective (also
probably real data to see!).  My biggest problem with the current
layout is that it stacks things that have different units making the
total height of the bars/plots meaningless.


Project View Page
-----------------

Could the number of buttons be reduced by having a "New..." drop down
list with "Project", "Machine Image", and "Deployment" as options?
This would remove some visual clutter.  For consistency, the same
could be used on the "Welcome" page with "Machine Image" and
"Deployment" deactivated.

I'd suggest that the title of this page should be the last element of
the resource; "CoolProject" in this case. 

Remove duplication in table between "Category" and the category icon.


Project Edit Page
-----------------

No additional comments.


Image View Page
---------------

The word "overview" is confusing as this doesn't provide an overview
of the given image, but of the relationships between this images and
others.

Graphically, this would be clearer as a hierarchy if arrows were used
to show the relationships.  The machine icon is confusing as these are
not really virtual machines (yet).

The interesting part of the resource names are at the end of the
names.  It would be better to cut the beginning of the resource name
rather than the middle.  Provide the full resource name in the tooltip
(which is currently empty). 

There is too much whitespace around the graphic and clicking on the
children moves the parent outside of the section boundry. 

Same comment about UUIDs in table as above. 

Title of page should again be last (last two?) elements of the
resource name.


Image Edit Page
---------------

For "Reference" section, change "Is base ..." to just "Base Image?".
Put the comment into a help bubble.

For the "Creation Recipes" section, it would be better to have
separate tabs for the "Recipe" and "Prerecipe".  


Deployment View Page
--------------------

I find it confusing to mix both nodes (virtual machines) with machine
images in the same diagram.  I would leave the details of the images
to the node definition section and not try to add this here.  It might
be useful to add parameter mapping if this can be done without making
the diagram too busy.

See previous comments about the graphic layout and labels.


Deployment Edit Page
--------------------

No additional comments.


Versions/History
----------------

No comments.


Run
---

The tooltips for the state machine boxes take the value of whatever
box was last hovered over.

See previous comments about the graphic in previous sections.

From the example, I'm not sure what information would be in each node
section.

Title should be the run UUID. 


Logout Page
-----------

Not in the repository.

Is a page actually needed?  Should it just clear the session and
redirect to the login page?  Presumably the logout link will bring up
a confirmation dialog. 


Configuration Page
------------------

Presumably this is a super user only page that would be accessed
through a link in the Top Red Menu Line?  

Should't the title and resource name be "Configuration"? 

Is there more than one "Configuration"?  If not, it should be
"Configuration" in the Top Red Menu Line.  (Even better, just an
icon.  Same for "Users".)


Documentation Page
------------------

This is one page that does not benefit at all from the accordian
layout.  This content would be more visible and shorter if just
formatted as a simple table or list. 

Why are there "Save" and "Reload Configuration File" buttons on this
page? 

Title should be "Documentation". 


Error Page
----------

I would add the warning icon as part of the banner and provide the
text of the error message in the banner subtitle.  There is no need
for a separate section here. 

The title should be "Error". 


Users Page
----------

I think that this page is more likely to be used to manage users than
to look at their usage.  A active, paged summary table with user
information would be more useful.  This should also provide the
ability to suspect and delete users as well as create new ones. 




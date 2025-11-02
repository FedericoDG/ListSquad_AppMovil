package com.federicodg80.listly.api;

import com.federicodg80.listly.api.auth.LoginRequest;
import com.federicodg80.listly.api.auth.LoginResponse;
import com.federicodg80.listly.api.auth.RebootRequest;
import com.federicodg80.listly.api.auth.RebootResponse;
import com.federicodg80.listly.api.invitation.InvitationRespondRequest;
import com.federicodg80.listly.api.invitation.InvitationRespondResponse;
import com.federicodg80.listly.api.invitation.InvitationSendRequest;
import com.federicodg80.listly.api.list.TaskListDetails;
import com.federicodg80.listly.api.list.TaskListMessage;
import com.federicodg80.listly.api.list.TaskListRequest;
import com.federicodg80.listly.api.settings.UpdateSettingsResponse;
import com.federicodg80.listly.api.subscription.SubscriptionResponse;
import com.federicodg80.listly.api.user.UserDetailsResponse;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.models.TaskList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Auth API
    @POST("auth/register")
    Call<LoginResponse> login(@Body LoginRequest request);

/*    @POST("auth/reboot")
    Call<RebootResponse> reboot(@Header("Authorization") String token, @Body RebootRequest request);*/

    // User API
    @GET("users/me")
    Call<UserDetailsResponse> getMe(@Header("Authorization") String token);

    // Lists API
    @GET("lists")
    Call<List<TaskList>> getLists(@Header("Authorization") String token);

    @POST("lists")
    Call<TaskList> createList(@Header("Authorization") String token, @Body TaskListRequest request);

    @GET("lists/{listId}")
    Call<TaskListDetails> getList(@Header("Authorization") String token, @Path("listId") int listId);

    @DELETE("lists/{listId}/collaborator/{collaboratorId}")
    Call<TaskListMessage> deleteCollaboratorFromList(
            @Header("Authorization") String token,
            @Path("listId") int listId,
            @Path("collaboratorId") String collaboratorId
    );

    @POST("lists/{listId}/items")
    Call<Item> addItemToList(@Header("Authorization") String token, @Path("listId") int listId, @Body Item item);

    // Items API
    @PATCH("items/{itemId}/toggle-completed")
    Call<Item> toggleCompleted(@Header("Authorization") String token, @Path("itemId") int listId);

    @DELETE("items/{itemId}")
    Call<Item> deleteItem(@Header("Authorization") String token, @Path("itemId") int listId);

    @GET("items/{itemId}")
    Call<Item> getItem(
            @Header("Authorization") String token,
            @Path("itemId") int itemId
    );

    @PUT("items/{itemId}")
    Call<Item> updateItem(
            @Header("Authorization") String token,
            @Path("itemId") int itemId,
            @Body Item item
    );

    // Subscription API
    @POST("subscriptions/monthly")
    Call<SubscriptionResponse> createPayment(@Header("Authorization") String token);

    // Settings API
    @PUT("settings/invitation-notifications")
    Call<UpdateSettingsResponse> updateInvitationNotificationsSetting(
            @Header("Authorization") String token,
            @Body boolean enabled
    );

    @PUT("settings/item-added-notifications")
    Call<UpdateSettingsResponse> updateItemAddedNotificationsSetting(
            @Header("Authorization") String token,
            @Body boolean enabled
    );

    @PUT("settings/item-status-changed-notifications")
    Call<UpdateSettingsResponse> updateItemStatusChangedNotificationsSetting(
            @Header("Authorization") String token,
            @Body boolean enabled
    );

    @PUT("settings/item-deleted-notifications")
    Call<UpdateSettingsResponse> updateItemDeletedNotificationsSetting(
            @Header("Authorization") String token,
            @Body boolean enabled
    );

    // Invitations API
    @GET("invitations/pending")
    Call<List<Invitation>> getPendingInvitations(@Header("Authorization") String token);

    @POST("invitations/{invitationId}/respond")
    Call<InvitationRespondResponse> respondToInvitation(
            @Header("Authorization") String token,
            @Path("invitationId") int invitationId,
            @Body InvitationRespondRequest response
    );

    @POST("invitations")
    Call<Invitation> sendInvitation(
            @Header("Authorization") String token,
            @Body InvitationSendRequest request
    );
}

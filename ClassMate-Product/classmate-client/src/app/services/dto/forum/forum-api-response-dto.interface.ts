import { ForumDTO } from "./forum-dto.interface";
import { PostRequestDTO } from "../post/post-request-dto.interface";
import { PostResponseDTO } from "../post/post-response-dto.interface";

export interface ForumApiResponseDTO{
  forum: ForumDTO;
  posts: PostResponseDTO[];
}

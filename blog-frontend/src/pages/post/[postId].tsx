import {useRouter} from "next/router";
import {MainPostDetail} from "@/components/Post";

export default function ShowPostDetailPage() {
    const router = useRouter();
    const {postId} = router.query;

    if (postId === undefined || Array.isArray(postId)) {
        return <div>Invalid post id provided</div>;
    }

    return (
        <MainPostDetail urlPath={postId}/>
    );
}
